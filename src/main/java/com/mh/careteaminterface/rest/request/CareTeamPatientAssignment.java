package com.mh.careteaminterface.rest.request;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
{
   "mrn":"M1234",
   "visitNumber":"V5678",
   "careTeam":[
      {
         "userIdentifier":"JYR123",
         "role":"Attending"
      },
      {
         "userIdentifier":"PTB789",
         "role":"Admitting"
      }
   ]
}
 */
public class CareTeamPatientAssignment implements Comparable<CareTeamPatientAssignment> {

  @NotNull(message = "Patient visit number cannot be null.")
  private String visitNumber;

  @NotNull(message = "Patient MRN cannot be null.")
  private String mrn;

  private Date lastUpdated;

  private List<Provider> careTeam = new ArrayList<>();

  public CareTeamPatientAssignment() {}

  public CareTeamPatientAssignment(String mrn, String visitNumber) {
    this.mrn = mrn;
    this.visitNumber = visitNumber;
  }

  public String getMrn() {
    return mrn;
  }

  public void setMrn(String mrn) {
    this.mrn = mrn;
  }

  public String getVisitNumber() {
    return visitNumber;
  }

  public void setVisitNumber(String visitNumber) {
    this.visitNumber = visitNumber;
  }

  public List<Provider> getCareTeam() {
    return careTeam;
  }

  public void setCareTeam(List<Provider> careTeam) {
    this.careTeam = careTeam;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void addProvider(Provider provider) {
    careTeam.add(provider);
  }

  @Override
  public int compareTo(CareTeamPatientAssignment o) {
    int compare = getLastUpdated().compareTo(o.getLastUpdated());
    return compare;
  }
}
