package com.mh.careteaminterface.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mh.careteaminterface.rest.request.CareTeamLocationAssignment;
import com.mh.careteaminterface.rest.request.CareTeamPatientAssignment;
import com.mh.careteaminterface.rest.request.ViewRequest;
import com.mh.careteaminterface.rest.response.BaseWsResponse;
import com.mh.careteaminterface.rest.response.OAuthResponse;
import com.mh.careteaminterface.rest.response.ViewResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Component
public class CTRestClient {

  @Value("${ctclient.baseurl}")
  private String baseUrl;

  @Value("${ctclient.ctapi.version}")
  private String ctapiVersion;

  @Value("${ctclient.authentication}")
  private boolean requiresAuthentication;

  private WebClient webClient;

  private static final String BEARER = "Bearer ";
  private static final Logger LOGGER = LoggerFactory.getLogger(CTRestClient.class);
  private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  @PostConstruct
  public void init(){
    webClient = WebClient
        .builder()
        .exchangeStrategies( // Explicitly enable header logging. By default, headers are masked.
            ExchangeStrategies.builder().codecs(c -> c.defaultCodecs().enableLoggingRequestDetails(true)).build())
        .baseUrl(baseUrl)
        .build();
  }

  public String authenticate(String authorizationHeader){

    OAuthResponse oAuthResponse =
        webClient
        .post()
        .uri("/auth")
        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromValue("grant_type=client_credentials"))
        .retrieve()
        .bodyToMono(OAuthResponse.class)
        .block();

    logJson(oAuthResponse);

    return BEARER + oAuthResponse.getAccess_token();

  }

  public ViewResponse viewCareTeam(ViewRequest viewRequest, String accessToken) {

    logJson(viewRequest);

    WebClient.RequestBodySpec requestBodySpec =
        webClient
            .post()
            .uri("/careTeamView");

    if (requiresAuthentication) {
      requestBodySpec.header(HttpHeaders.AUTHORIZATION, accessToken);
    }

    ViewResponse viewResponse =
        requestBodySpec
            .header(HttpHeaders.ACCEPT, ctapiVersion)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(viewRequest))
            .retrieve()
            .bodyToMono(ViewResponse.class)
            .block();

    logJson(viewResponse);
    return viewResponse;

  }

  public BaseWsResponse makePatientAssignment(CareTeamPatientAssignment patientAssignment, String accessToken) {

    WebClient.RequestBodySpec requestBodySpec =
        webClient
            .put()
            .uri("/careTeamAssignment");

    return getBasWsResponse(requestBodySpec, patientAssignment, accessToken);

  }

  public BaseWsResponse makeLocationAssignment(CareTeamLocationAssignment locationAssignment, String accessToken) {

    WebClient.RequestBodySpec requestBodySpec =
        webClient
            .put()
            .uri("/careTeamLocationAssignment");

    return getBasWsResponse(requestBodySpec,locationAssignment, accessToken);

  }

  private BaseWsResponse getBasWsResponse(WebClient.RequestBodySpec requestBodySpec, Object assignment, String accessToken){

    logJson(assignment);

    if (requiresAuthentication) {
      requestBodySpec.header(HttpHeaders.AUTHORIZATION, accessToken);
    }

    BaseWsResponse baseWsResponse =
        requestBodySpec
            .header(HttpHeaders.ACCEPT, ctapiVersion)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(assignment))
            .retrieve()
            .bodyToMono(BaseWsResponse.class)
            .block();

    logJson(baseWsResponse);

    return baseWsResponse;

  }

  private void logJson(Object ob){
    try{
      String json = MAPPER.writeValueAsString(ob);
      LOGGER.info(json);
    }
    catch (JsonProcessingException e){}
  }


}
