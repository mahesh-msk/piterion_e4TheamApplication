package theme_plugin_project.customwidget;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.steadystate.css.dom.Property;

import javafx.scene.text.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

public class UserProfileWidget extends Composite {
    private ResourceManager resourceManager;

    private String text = "Sample Description";
    private String headerText = "Hello World";
    private ImageDescriptor imageDescriptor;

    private Label imageLabel;
    private Label headerLabel;
    private Label labelText;

    private Composite head;
    private Button btnNewButton;

	private Menu popupMenu;

	private MenuItem newItem;

    public UserProfileWidget(Composite parent, int style) {
        super(parent, style);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(this);

        head = new Composite(this, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(head);
        GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(head);
        headerLabel = new Label(head, SWT.NONE);
        headerLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        headerLabel.setText(getHeaderText());

        Composite imageComposite = new Composite(this, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(imageComposite);
        GridDataFactory.fillDefaults().applyTo(imageComposite);
        imageLabel = new Label(imageComposite, SWT.NONE);
        ImageDescriptor imgDescriptor = getImage();
        if (imgDescriptor != null) {
            imageLabel.setImage(getResourceManager().createImage(imgDescriptor));
        }

        Composite textComposite = new Composite(this, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(textComposite);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(textComposite);
        textComposite.setLayout(new GridLayout(3, false));
        labelText = new Label(textComposite, SWT.NONE);
        labelText.setText(getText());
        new Label(textComposite, SWT.NONE);
        new Label(textComposite, SWT.NONE);
        new Label(textComposite, SWT.NONE);
        new Label(textComposite, SWT.NONE);
        new Label(textComposite, SWT.NONE);
        new Label(textComposite, SWT.NONE);
        new Label(textComposite, SWT.NONE);
        
        textComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
        
        btnNewButton = new Button(textComposite, SWT.NONE);
        btnNewButton.setText("Application Button");
        
        popupMenu = new Menu(btnNewButton);
        newItem = new MenuItem(popupMenu, SWT.CASCADE);
        newItem.setText("New");
        MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
        refreshItem.setText("Refresh");
        MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
        deleteItem.setText("Delete");


      //  newItem.setData("org.eclipse.e4.ui.css.id", "MyCSSTagForlblTheme");

        Menu newMenu = new Menu(popupMenu);
        newItem.setMenu(newMenu);

        MenuItem shortcutItem = new MenuItem(newMenu, SWT.NONE);
        shortcutItem.setText("Shortcut");
        MenuItem iconItem = new MenuItem(newMenu, SWT.NONE);
        iconItem.setText("Icon");

        btnNewButton.setMenu(popupMenu);
        
        
    }

    protected ResourceManager getResourceManager() {
        if (null == resourceManager) {
            resourceManager = new LocalResourceManager(JFaceResources.getResources(), this);
        }
        return resourceManager;
    }

    protected String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if(labelText != null) {
            labelText.setText(text);
        }
    }

    protected String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
        if (headerLabel != null) {
            headerLabel.setText(headerText);
        }
    }

    protected ImageDescriptor getImage() {
        return imageDescriptor;
    }

    public void setImage(ImageDescriptor imageDescriptor) {
        this.imageDescriptor = imageDescriptor;
        if (imageLabel != null) {
            imageLabel.setImage(getResourceManager().createImage(imageDescriptor));
        }
    }

    public Color getHeaderColor() {
        return head.getBackground();
        
    }

    public void setHeaderColor(Color color) {
        head.setBackground(color);
        headerLabel.setBackground(color);
     
      
     
    }
    
    public Color getBackground() {
        return btnNewButton.getBackground();
    }

    public void setBackground(Color color) {
    	btnNewButton.setBackground(color);
 
    }

    
}