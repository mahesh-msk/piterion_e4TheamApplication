/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theme_plugin_project.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */

public class UserHistoryTbl implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	//Filter : Range : Start and End : Nebula CDate
	private Date logTime;

	//Filter : Text
	private String userName;

	//Filter : Text
	private String host;

	//Filter : Text
	private String site;

	//Filter : Text
	private String adminArea;

	//Filter : Text
	private String project;

	//Filter : Text
	private String application;

	private Integer pid;
	
	//Filter : Combo [ENUM] 
	private String result;
	private String args;

	private String isUserStatus;

	public UserHistoryTbl() {
	}

	public UserHistoryTbl(Long id) {
		this.id = id;
	}

	public UserHistoryTbl(Long id, Date logTime, String isUserStatus) {
		this.id = id;
		this.logTime = logTime;
		this.isUserStatus = isUserStatus;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getAdminArea() {
		return adminArea;
	}

	public void setAdminArea(String adminArea) {
		this.adminArea = adminArea;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getIsUserStatus() {
		return isUserStatus;
	}

	public void setIsUserStatus(String isUserStatus) {
		this.isUserStatus = isUserStatus;
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
		if (!(object instanceof UserHistoryTbl)) {
			return false;
		}
		UserHistoryTbl other = (UserHistoryTbl) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.magna.xmbackend.entities.UserHistoryTbl[ id=" + id + " ]";
	}

}
