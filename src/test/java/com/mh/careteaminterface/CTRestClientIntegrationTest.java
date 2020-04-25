package com.mh.careteaminterface;

import com.mh.careteaminterface.rest.request.CareTeamLocationAssignment;
import com.mh.careteaminterface.rest.request.CareTeamPatientAssignment;
import com.mh.careteaminterface.rest.request.Provider;
import com.mh.careteaminterface.rest.request.ViewRequest;
import com.mh.careteaminterface.rest.response.BaseWsResponse;
import com.mh.careteaminterface.rest.response.OAuthResponse;
import com.mh.careteaminterface.rest.response.ViewResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Integration testing with the full application context.
@AutoConfigureWebTestClient
public class CTRestClientIntegrationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(CTRestClientIntegrationTest.class);

  private static String accessToken;
  private static String baseUrl;
  private static String ctapiVersion;
  private static boolean requiresAuthentication;

  private static WebTestClient webTestClient;

  @BeforeAll
  public static void setUp(@Value("${ctclient.baseurl}") String baseUrlValue,
                           @Value("${ctclient.ctapi.version}") String ctapiVersionValue,
                           @Value("${ctclient.authentication}") boolean requiresAuthenticationValue){

    baseUrl = baseUrlValue;
    ctapiVersion = ctapiVersionValue;
    requiresAuthentication = requiresAuthenticationValue;

    LOGGER.info("Setting up!");

    webTestClient =
        WebTestClient
        .bindToServer()
        .responseTimeout(Duration.ofMillis(10000))
        .exchangeStrategies( // Explicitly enable header logging. By default, headers are masked.
            ExchangeStrategies.builder().codecs(c -> c.defaultCodecs().enableLoggingRequestDetails(true)).build())
        .build();

    LOGGER.info("Setup complete!");

    accessToken = "Bearer " + getAccessToken();
  }

  @Test
  public void testPatientAssignment() {

    LOGGER.info("Making a patient assignment.");

    CareTeamPatientAssignment assignment = buildPatientAssignment();

    WebTestClient.RequestBodySpec requestBodySpec =
        webTestClient
            .put()
            .uri(baseUrl+ "/careTeamAssignment");

    if(requiresAuthentication) {
      requestBodySpec.header(HttpHeaders.AUTHORIZATION, accessToken);
    }

    EntityExchangeResult<BaseWsResponse> result =
        requestBodySpec
            .header(HttpHeaders.ACCEPT, ctapiVersion)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(assignment))
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(BaseWsResponse.class)
            .returnResult();

    assertNotNull(result.getResponseBody());

    LOGGER.info("Done making patient assignment.");

  }

  @Test
  public void testLocationAssignment() {

    LOGGER.info("Making a location assignment.");

    CareTeamLocationAssignment assignment = buildLocationAssignment();

    WebTestClient.RequestBodySpec requestBodySpec =
        webTestClient
            .put()
            .uri(baseUrl+ "/careTeamLocationAssignment");

    if(requiresAuthentication) {
      requestBodySpec.header(HttpHeaders.AUTHORIZATION, accessToken);
    }

    EntityExchangeResult<BaseWsResponse> result =
        requestBodySpec
            .header(HttpHeaders.ACCEPT, ctapiVersion)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(assignment))
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(BaseWsResponse.class)
            .returnResult();

    assertNotNull(result.getResponseBody());

    LOGGER.info("Done making a location assignment.");

  }

  @Test
  public void testViewCareTeam(){

    LOGGER.info("Viewing a care team.");

    ViewRequest request = new ViewRequest();
    request.setRequestId("123");
    request.setVisitNumber("V5578859357879962186");
    request.setMrn("M9748337564706146629");

    WebTestClient.RequestBodySpec requestBodySpec =
        webTestClient
            .post()
            .uri(baseUrl+ "/careTeamView");

    if(requiresAuthentication) {
      requestBodySpec.header(HttpHeaders.AUTHORIZATION, accessToken);
    }

    EntityExchangeResult<ViewResponse> result =
        requestBodySpec
            .header(HttpHeaders.ACCEPT, ctapiVersion)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(ViewResponse.class)
            .returnResult();
    
    assertNotNull(result.getResponseBody());

    LOGGER.info("Done viewing a care team.");

  }

  private CareTeamPatientAssignment buildPatientAssignment(){
    CareTeamPatientAssignment assignment = new CareTeamPatientAssignment();
    assignment.setVisitNumber("V5578859357879962186");
    assignment.setMrn("M9748337564706146629");

    assignment.setCareTeam(buildProviderList());

    return assignment;
  }

  private CareTeamLocationAssignment buildLocationAssignment(){
    CareTeamLocationAssignment assignment = new CareTeamLocationAssignment();
    assignment.setLocationId("ct_u1_r1_bA");
    assignment.setCareTeam(buildProviderList());
    return assignment;
  }

  private List<Provider> buildProviderList(){
    Provider provider1 = new Provider();
    provider1.setUserIdentifier("valerie1");
    provider1.setRole("PCP");

    Provider provider2 = new Provider();
    provider2.setUserIdentifier("valerie2");
    provider2.setRole("PCP");

    return Arrays.asList(provider1, provider2);
  }

  private static String getAccessToken() {

    EntityExchangeResult<OAuthResponse> result =
        webTestClient
            .post()
            .uri(baseUrl+ "/auth")
            .header(HttpHeaders.AUTHORIZATION, "Basic Y2FyZXRlYW1hdXRodXNlcjpjaGFuZ2VtZQ==")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromValue("grant_type=client_credentials"))
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(OAuthResponse.class)
            .returnResult();

    String accessToken = result.getResponseBody().getAccess_token();

    assertNotNull(accessToken);

    return accessToken;

  }
}
