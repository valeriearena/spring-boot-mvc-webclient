package com.mh.careteaminterface.service;

import com.mh.careteaminterface.controller.model.AccessTokenBean;
import com.mh.careteaminterface.rest.CTRestClient;
import com.mh.careteaminterface.rest.request.CareTeamLocationAssignment;
import com.mh.careteaminterface.rest.request.CareTeamPatientAssignment;
import com.mh.careteaminterface.rest.request.Provider;
import com.mh.careteaminterface.rest.request.ViewRequest;
import com.mh.careteaminterface.rest.response.BaseWsResponse;
import com.mh.careteaminterface.rest.response.ViewResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CTClientService {

  @Autowired
  private CTRestClient ctRestClient;

  private static final Logger LOGGER = LoggerFactory.getLogger(CTClientService.class);

  public String authenticate(AccessTokenBean accessTokenBean){

    String userPW = accessTokenBean.getUserName() + ":" + accessTokenBean.getPassword();
    String base64encoded = Base64.getEncoder().encodeToString(userPW.getBytes());

    return ctRestClient.authenticate(base64encoded);

  }

  public ViewResponse getCareTeam(ViewRequest viewRequest, String accessToken) {

    viewRequest.setRequestId(RandomStringUtils.random(6, true, true));
    return ctRestClient.viewCareTeam(viewRequest, accessToken);

  }

  public BaseWsResponse makePatientAssignment(CareTeamPatientAssignment careTeamPatientAssignment, String accessToken) {

    careTeamPatientAssignment.setCareTeam(removeNullProviders(careTeamPatientAssignment.getCareTeam()));
    return ctRestClient.makePatientAssignment(careTeamPatientAssignment, accessToken);

  }

  public BaseWsResponse makeLocationAssignment(CareTeamLocationAssignment locationAssignmentBean, String accessToken) {

    locationAssignmentBean.setCareTeam(removeNullProviders(locationAssignmentBean.getCareTeam()));
    return ctRestClient.makeLocationAssignment(locationAssignmentBean, accessToken);

  }

  public CTRestClient getCtRestClient() {
    return ctRestClient;
  }

  public void setCtRestClient(CTRestClient ctRestClient) {
    this.ctRestClient = ctRestClient;
  }

  private List<Provider> removeNullProviders(List<Provider> providerList){

    return providerList.stream()
        .filter(p -> !StringUtils.isBlank(p.getUserIdentifier()))
        .collect(Collectors.toList());
  }

}
