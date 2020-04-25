package com.mh.careteaminterface.rest.response;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExternalProvider {

  private String fullName;
  private List<String> phoneLabelNumberList = new ArrayList<>();
  private String assignmentTimestamp;
  private String encodedPhoneList;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEncodedPhoneList() {
    return encodedPhoneList;
  }

  public void setEncodedPhoneList(String encodedPhoneList) {
    this.encodedPhoneList = encodedPhoneList;
  }

  public List<String> getPhoneLabelNumberList() {
    return phoneLabelNumberList;
  }

  public void setPhoneLabelNumberList(List<String> phoneLabelNumberList) {
    this.phoneLabelNumberList = phoneLabelNumberList;
  }

  public String getAssignmentTimestamp() {
    return assignmentTimestamp;
  }

  public void setAssignmentTimestamp(Timestamp assignmentTimestamp) {
    if (assignmentTimestamp != null) {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      ZonedDateTime zonedDateTime = assignmentTimestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
      this.assignmentTimestamp = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).format(dateTimeFormatter);
    }
  }
}
