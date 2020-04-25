package com.mh.careteaminterface.rest.response;

public class ViewResponse {

  private boolean success;
  private int statusCode;
  private String message;

  private String requestId;

  private InternalProvider[] internalProviders = new InternalProvider[0];

  private ExternalProvider[] externalProviders = new ExternalProvider[0];

  public ViewResponse() {}

  public ViewResponse(boolean success, int statusCode, String message) {
    this.success = success;
    this.statusCode = statusCode;
    this.message = message;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public InternalProvider[] getInternalProviders() {
    return internalProviders;
  }

  public void setInternalProviders(InternalProvider[] internalProviders) {
    this.internalProviders = internalProviders;
  }

  public ExternalProvider[] getExternalProviders() {
    return externalProviders;
  }

  public void setExternalProviders(ExternalProvider[] externalProviders) {
    this.externalProviders = externalProviders;
  }

  @Override
  public String toString() {

    return String.format("ViewResponse{success=%s, statusCode=%d, message=%s, requestId=%s}", success, statusCode, message, requestId);
  }
}
