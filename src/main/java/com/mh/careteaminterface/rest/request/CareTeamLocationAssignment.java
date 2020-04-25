package com.mh.careteaminterface.rest.request;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
{
   "locationId":"ct1_loc_bed1",
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
public class CareTeamLocationAssignment implements Comparable<CareTeamLocationAssignment> {

  @NotNull(message = "Bed location cannot be null.")
  private String locationId;

  private String facilityId = "Default";
  private Date lastUpdated;

  private List<Provider> careTeam = new ArrayList<>();

  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
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

  public String getFacilityId() {
    return facilityId;
  }

  public void setFacilityId(String facilityId) {
    this.facilityId = facilityId;
  }

  public void addProvider(Provider provider) {
    careTeam.add(provider);
  }

  @Override
  public int compareTo(CareTeamLocationAssignment o) {
    int compare = getLastUpdated().compareTo(o.getLastUpdated());
    return compare;
  }
}