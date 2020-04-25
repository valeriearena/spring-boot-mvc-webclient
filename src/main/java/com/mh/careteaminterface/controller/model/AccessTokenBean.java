package com.mh.careteaminterface.controller.model;

import javax.validation.constraints.NotNull;

public class AccessTokenBean {

  @NotNull(message = "User name cannot be null.")
  private String userName;

  @NotNull(message = "Password cannot be null.")
  private String password;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
