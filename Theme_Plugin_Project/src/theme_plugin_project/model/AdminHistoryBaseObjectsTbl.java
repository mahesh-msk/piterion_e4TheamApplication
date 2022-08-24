/**
 * 
 */
package theme_plugin_project.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Bhabadyuti Bal
 *
 */
public class AdminHistoryBaseObjectsTbl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	//Filter : Range : Start and End : Nebula CDate
	private Date logTime;
	//Filter : Text
	private String adminName;
	private String apiRequestPath;
	
	//Filter : Combo [ENUM] 
	private String operation;
	
	//Filter : Text
	private String objectName;

	//On Nattable : To open some UI to display content in readable fashion
	private String changes;
	//On Nattable : To open some UI to display content in readable fashion
	private String errorMessage;
	
	//Filter : Combo [ENUM] 
	private String result;

	@SuppressWarnings("unused")
	private Long long1;

	@SuppressWarnings("unused")
	private Date date;

	@SuppressWarnings("unused")
	private String string;

	@SuppressWarnings("unused")
	private String string2;

	@SuppressWarnings("unused")
	private String string3;

	
	public AdminHistoryBaseObjectsTbl(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getApiRequestPath() {
		return apiRequestPath;
	}

	public void setApiRequestPath(String apiRequestPath) {
		this.apiRequestPath = apiRequestPath;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getChanges() {
		return changes;
	}

	public void setChanges(String changes) {
		this.changes = changes;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof AdminHistoryBaseObjectsTbl)) {
			return false;
		}
		AdminHistoryBaseObjectsTbl other = (AdminHistoryBaseObjectsTbl) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.magna.xmbackend.entities.AdminHistoryBaseObjectsTbl[ id=" + id + " ]";
	}

}
