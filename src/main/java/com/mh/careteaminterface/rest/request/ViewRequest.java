package com.mh.careteaminterface.rest.request;

import javax.validation.constraints.NotNull;

public class ViewRequest {

  @NotNull(message = "Patient visit number cannot be null.")
  private String visitNumber;

  private String mrn;

  private String requestId;

  public ViewRequest(){}

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
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


}
