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
public class AdminHistoryRelationsTbl implements Serializable {

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
    private String adminArea;
    
    //Filter : Text
    private String project;
    
    //Filter : Text
    private String projectApplication;
    
    //Filter : Text
    private String userName;
    
    //Filter : Text
    private String startApplication;
    
    //Filter : Combo [ENUM] 
    private String relationType;
    
    //Filter : Combo [ENUM]
    private String status;
    
  //Filter : Text
    private String site;
    
  //Filter : Text
    private String groupName;
    
  //Filter : Text
    private String userApplication;
    
  //Filter : Text
    private String directory;
    
  //Filter : Text
    private String role;
    
  //On Nattable : To open some UI to display content in readable fashion
    private String errorMessage;
    
  //Filter : Combo [ENUM] 
    private String result;

    public AdminHistoryRelationsTbl() {
    }

    public AdminHistoryRelationsTbl(Long id) {
        this.id = id;
    }

    public AdminHistoryRelationsTbl(Long id, Date logTime, String adminName, String apiRequestPath, String operation) {
        this.id = id;
        this.logTime = logTime;
        this.adminName = adminName;
        this.apiRequestPath = apiRequestPath;
        this.operation = operation;
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

    public String getUserApplication() {
        return userApplication;
    }

    public void setUserApplication(String userApplication) {
        this.userApplication = userApplication;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProjectApplication() {
        return projectApplication;
    }

    public void setProjectApplication(String projectApplication) {
        this.projectApplication = projectApplication;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartApplication() {
        return startApplication;
    }

    public void setStartApplication(String startApplication) {
        this.startApplication = startApplication;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdminHistoryRelationsTbl)) {
            return false;
        }
        AdminHistoryRelationsTbl other = (AdminHistoryRelationsTbl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.magna.xmbackend.entities.AdminHistoryRelationsTbl[ id=" + id + " ]";
    }
    
}
