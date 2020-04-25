package com.mh.careteaminterface.rest.response;

public class BaseWsResponse {

  private boolean success;
  private int statusCode;
  private String message;

  public BaseWsResponse() {
  }

  public BaseWsResponse(boolean success, int statusCode, String message) {
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


  @Override
  public String toString() {

    return String.format("BaseWsResponse{success=%s, statusCode=%d, message='%s'}", success,
        statusCode, message);
  }
}
