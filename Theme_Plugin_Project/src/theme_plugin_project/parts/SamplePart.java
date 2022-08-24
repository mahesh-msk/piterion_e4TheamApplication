package theme_plugin_project.parts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.css.core.dom.ExtendedDocumentCSS;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.Preferences;
import org.w3c.css.sac.CSSParseException;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;


@SuppressWarnings("restriction")
public class SamplePart {

	/** The eclipse context. */
	@Inject
	private IEclipseContext eclipseContext;

	/** Member variable 'xm menu part action' for {@link XmMenuPartAction}. */
	private SamplePartAction samplePartAction;

	/**
	 * Post construct.
	 *
	 * @param parent
	 *            the parent
	 */
	@PostConstruct
	public void postConstruct(final Composite parent, IThemeEngine engine) {
		try {
			parent.setLayout(new GridLayout(1, false));
			parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			Preferences preferences = InstanceScope.INSTANCE.getNode("Theme_Plugin_Project");
			Preferences sub1 = preferences.node("node1");

			// System.out.println(sub1.get("cusstomCss", "default"));

			if (sub1.get("cusstomCssName", "default").equals("custom.css")) {

				File file = new File(sub1.get("cusstomCssPath", "default"));
				applyCSS(engine, file);
			}

			buildComponents(parent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void applyCSS(IThemeEngine themeEngine, File cssEngine)
			throws IOException, InvocationTargetException, InterruptedException {
		if (themeEngine == null) {

			return;
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask("Applying CSS", 100);
				try {

					long start = System.nanoTime();

					StringBuilder sb = new StringBuilder();

					((ThemeEngine) themeEngine).resetCurrentTheme();

					int count = 0;
					for (CSSEngine engine : ((ThemeEngine) themeEngine).getCSSEngines()) {
						if (count++ > 0) {
							sb.append("\n\n");
						}
						sb.append("Engine[").append(engine.getClass().getSimpleName()).append("]");
						ExtendedDocumentCSS doc = (ExtendedDocumentCSS) engine.getDocumentCSS();
						List<StyleSheet> sheets = new ArrayList<>();
						StyleSheetList list = doc.getStyleSheets();
						for (int i = 0; i < list.getLength(); i++) {
							sheets.add(list.item(i));
							monitor.worked(20);
						}

						FileInputStream fileInputStream = null;
						try {
							fileInputStream = new FileInputStream(cssEngine);
							sheets.add(engine.parseStyleSheet(fileInputStream));
							doc.removeAllStyleSheets();
							for (StyleSheet sheet : sheets) {
								doc.addStyleSheet(sheet);
							}
							monitor.worked(20);
							engine.reapply();
							monitor.worked(30);
							long nanoDiff = System.nanoTime() - start;
							sb.append("\nTime: ").append(nanoDiff / 1000000).append("ms");
							monitor.worked(10);
						} catch (CSSParseException e) {
							sb.append("\nError: line ").append(e.getLineNumber()).append(" col ")
									.append(e.getColumnNumber()).append(": ").append(e.getLocalizedMessage());
						} catch (IOException e) {
							sb.append("\nError: ").append(e.getLocalizedMessage());
						} finally {
							try {
								Thread.sleep(5000);
								monitor.worked(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							fileInputStream.close();
							monitor.worked(10);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				monitor.done();
			}

		};

		new ProgressMonitorDialog(new Shell()).run(false, false, op);

	}

	/**
	 * Build components.
	 *
	 * @param parent
	 *            the parent
	 */
	private void buildComponents(Composite parent) {
		eclipseContext.set(Composite.class, parent);
		samplePartAction = ContextInjectionFactory.make(SamplePartAction.class, eclipseContext);
	}

	/**
	 * Gets the process id.
	 *
	 * @param process
	 *            the process
	 * @return the process id
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	String getProcessId(Process process) throws IOException {
		System.out.println(ManagementFactory.getRuntimeMXBean().getName());
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

		while ((line = in.readLine()) != null) {
			String[] javaProcess = line.split(" ");
			if (javaProcess.length > 1 && javaProcess[1].endsWith("ClassName")) {
				System.out.println("pid => " + javaProcess[0]);
				System.out.println("Fully Qualified Class Name => " + javaProcess[1]);
			}
		}

		String pid = "Unable to find PID";
		return pid;
	}

	/**
	 * Gets the xm menu part action.
	 *
	 * @return the xm menu part action
	 */
	public SamplePartAction getXmMenuPartAction() {
		return samplePartAction;
	}

	/**
	 * Sets the xm menu part action.
	 *
	 * @param xmMenuPartAction
	 *            the new xm menu part action
	 */
	public void setXmMenuPartAction(SamplePartAction xmMenuPartAction) {
		this.samplePartAction = xmMenuPartAction;
	}
}