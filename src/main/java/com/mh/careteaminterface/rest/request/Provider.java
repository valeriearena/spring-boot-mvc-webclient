package com.mh.careteaminterface.rest.request;

/**
 * Created by valerie on 10/27/16.
 */

/*
      {
         "userIdentifier":"JYR123",
         "role":"Attending"
      }
 */
public class Provider {

  private String userIdentifier;
  private String role;

  public Provider() {}

  public Provider(String userIdentifier, String role) {
    this.userIdentifier = userIdentifier;
    this.role = role;
  }

  public String getUserIdentifier() {
    return userIdentifier;
  }

  public void setUserIdentifier(String userIdentifier) {
    this.userIdentifier = userIdentifier;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}

