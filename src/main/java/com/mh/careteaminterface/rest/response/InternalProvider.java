package com.mh.careteaminterface.rest.response;

public class InternalProvider {

  private String userIdentifier;
  private String fullName;
  private String userRole;
  private String userStatus;
  private String patientSpecificRole;
  private String assignmentTimestamp;

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public void setUserIdentifier(String userIdentifier) {
    this.userIdentifier = userIdentifier;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public String getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(String userStatus) {
    this.userStatus = userStatus;
  }

  public String getPatientSpecificRole() {
    return patientSpecificRole;
  }

  public void setPatientSpecificRole(String patientSpecificRole) {
    this.patientSpecificRole = patientSpecificRole;
  }

  public String getAssignmentTimestamp() {
    return assignmentTimestamp;
  }

  public void setAssignmentTimestamp(String assignmentTimestamp) {
    this.assignmentTimestamp = assignmentTimestamp;
  }
}
