package com.mh.careteaminterface.controller;

import com.mh.careteaminterface.controller.model.AccessTokenBean;
import com.mh.careteaminterface.rest.request.CareTeamLocationAssignment;
import com.mh.careteaminterface.rest.request.CareTeamPatientAssignment;
import com.mh.careteaminterface.rest.request.Provider;
import com.mh.careteaminterface.rest.request.ViewRequest;
import com.mh.careteaminterface.rest.response.BaseWsResponse;
import com.mh.careteaminterface.rest.response.ViewResponse;
import com.mh.careteaminterface.service.CTClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/ctassignments") // Maps http://localhost:8080/ctassignments to this controller
@SessionAttributes("accessToken")
public class CTClientController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CTClientController.class);

  private static final String MAPPING_TOKEN = "token";
  private static final String MAPPING_VIEW = "view";
  private static final String MAPPING_PATIENT = "patient";
  private static final String MAPPING_LOCATION = "location";

  private static final String VIEW_HOME = "home";
  private static final String VIEW_ACCESS_TOKEN = "access-token";
  private static final String VIEW_VIEW_CARETEAM = "careteam-view";
  private static final String VIEW_PATIENT_ASSIGNMENT = "patient-assignment";
  private static final String VIEW_LOCATION_ASSIGNMENT = "location-assignment";
  private static final String VIEW_ERROR = "error";

  private static final String MESSAGE_SUCCESS = "successMessage";
  private static final String MESSAGE_ERROR = "errorMessage";

  @Autowired
  private CTClientService ctClientService;

  @GetMapping // Maps GET request to specific handler method. Shortcut for @RequestMapping(method = RequestMethod.GET)
  public String displayHomeScreen() {
    return VIEW_HOME;
  }

  @GetMapping(MAPPING_TOKEN)
  public String displayAccessTokenScreen(Model model) {
    model.addAttribute(new AccessTokenBean());
    return VIEW_ACCESS_TOKEN;
  }

  @GetMapping(MAPPING_VIEW)
  public String displayCareTeamScreen(Model model) {
    model.addAttribute(new ViewRequest());
    model.addAttribute(new ViewResponse());
    return VIEW_VIEW_CARETEAM;
  }

  @GetMapping(MAPPING_PATIENT)
  public String displayPatientAssignmentScreen(Model model) {
    model.addAttribute(careTeamPatientAssignment());
    return VIEW_PATIENT_ASSIGNMENT;
  }

  @GetMapping(MAPPING_LOCATION)
  public String displayLocationAssignmentScreen(Model model) {
    model.addAttribute(careTeamLocationAssignment());
    return VIEW_LOCATION_ASSIGNMENT;
  }

  /**
   *  Get the access token header that is submitted in requests that require authentication.
   *
   * @param accessTokenBean The model attribute is populated with data from a form submitted.
   *                        When used as a method argument, it indicates the argument should be retrieved from the model.
   * @param bindingResult The bindingResult argument holds the result of a validation and binding and contains errors that may have occurred.
   *                      Must come right after the model object that is validated or else Spring fails to validate the object and throws an exception.
   * @param model The model is a holder for model attributes that are used for rendering views.
   * @return the view
   */
  @PostMapping(MAPPING_TOKEN)
  public String getAccessToken(@SessionAttribute(required = false) String accessToken, @ModelAttribute @Valid AccessTokenBean accessTokenBean, BindingResult bindingResult, Model model) {

    LOGGER.info("Retrieving access token: User Name = {}, Password = {}", accessTokenBean.getUserName(), accessTokenBean.getPassword());

    if (bindingResult.hasErrors()) {
      LOGGER.error("Validation error: User Name = {}, Password = {}", accessTokenBean.getUserName(), accessTokenBean.getPassword());
      return VIEW_ACCESS_TOKEN;
    }

    accessToken = ctClientService.authenticate(accessTokenBean);
    model.addAttribute("accessToken", accessToken);
    String successMessage = String.format("Retrieving access token successful! Access token %s will be automatically added to your requests if ctclient.authentication is set to true.", "");
    model.addAttribute(MESSAGE_SUCCESS, successMessage);

    LOGGER.info("Done retrieving access token: User Name = {}, Password = {}", accessTokenBean.getUserName(), accessTokenBean.getPassword());

    return VIEW_ACCESS_TOKEN;
  }

  @PostMapping(MAPPING_VIEW)
  public String getCareTeam(
      @SessionAttribute(required = false) String accessToken,
      @ModelAttribute @Valid ViewRequest viewRequest,
      BindingResult bindingResult, Model model) {

    LOGGER.info("Retrieving patient care team: Patient Visit Number = {}", viewRequest.getVisitNumber());

    if (bindingResult.hasErrors()) {
      model.addAttribute(new ViewResponse());
      return VIEW_VIEW_CARETEAM;
    }

    ViewResponse viewResponse = ctClientService.getCareTeam(viewRequest, accessToken);

    if (viewResponse.isSuccess()) {
      String successMessage =
          String.format("Retrieving care team for %s successful! %d caregivers were found.",
              viewRequest.getVisitNumber(), (viewResponse.getInternalProviders().length + viewResponse.getExternalProviders().length));

      model.addAttribute(MESSAGE_SUCCESS, successMessage);
      model.addAttribute(viewResponse);

    } else {
      String errorMessage = String.format("Retrieving care team for %s failed. Error message: %s", viewRequest.getVisitNumber(), viewResponse.getMessage());
      model.addAttribute(MESSAGE_ERROR, errorMessage);
    }

    LOGGER.info("Done retrieving patient care team: Patient Visit Number = {}", viewRequest.getVisitNumber());

    return VIEW_VIEW_CARETEAM;

  }

  @PostMapping(MAPPING_PATIENT)
  public String makePatientAssignment(
      @SessionAttribute(required = false) String accessToken,
      @ModelAttribute @Valid CareTeamPatientAssignment careTeamPatientAssignment,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    LOGGER.info("Making patient assignment: Patient Visit Number = {}", careTeamPatientAssignment.getVisitNumber());

    if (bindingResult.hasErrors()) {
      return VIEW_PATIENT_ASSIGNMENT;
    }

    BaseWsResponse baseWsResponse = ctClientService.makePatientAssignment(careTeamPatientAssignment, accessToken);
    if (baseWsResponse.isSuccess()) {
      addSuccessMessage(careTeamPatientAssignment.getVisitNumber(), careTeamPatientAssignment.getCareTeam(), redirectAttributes);

    } else {
      addErrorMessage(careTeamPatientAssignment.getVisitNumber(), baseWsResponse.getMessage(), redirectAttributes);
    }

    LOGGER.info("Done making patient assignment: Patient Visit Number = {}", careTeamPatientAssignment.getVisitNumber());

    return "redirect:/ctassignments/" + MAPPING_PATIENT;
  }

  @PostMapping(MAPPING_LOCATION)
  public String makeLocationAssignment(
      @SessionAttribute(required = false) String accessToken,
      @ModelAttribute @Valid CareTeamLocationAssignment careTeamLocationAssignment,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    LOGGER.info("Making patient assignment: Bed Location = {}", careTeamLocationAssignment.getLocationId());

    if (bindingResult.hasErrors()) {
      LOGGER.error("Validation error: Bed Location = {}", careTeamLocationAssignment.getLocationId());
      return VIEW_LOCATION_ASSIGNMENT;
    }

    BaseWsResponse baseWsResponse = ctClientService.makeLocationAssignment(careTeamLocationAssignment, accessToken);

    if (baseWsResponse.isSuccess()) {
      addSuccessMessage(careTeamLocationAssignment.getLocationId(), careTeamLocationAssignment.getCareTeam(), redirectAttributes);

    } else {
      addErrorMessage(careTeamLocationAssignment.getLocationId(), baseWsResponse.getMessage(), redirectAttributes);
    }

    LOGGER.info("Done making patient assignment: Bed Location = {}", careTeamLocationAssignment.getLocationId());

    return "redirect:/ctassignments/" + MAPPING_LOCATION;
  }

  @ExceptionHandler(WebClientResponseException.class)
  public String WebClientResponseException(WebClientResponseException ex, Model model) {

    String errorMessage = String.format("Error from WebClient: Status Code = %d, Error Message = %s", ex.getStatusCode().value(), ex.getResponseBodyAsString());
    LOGGER.error("Error from WebClient: {}", errorMessage);

    model.addAttribute(MESSAGE_ERROR, errorMessage);

    return VIEW_ERROR;
  }

  /**
   * @InitBinder annotated methods are used to customize request parameters being sent to the controller.
   *
   * @param binder WebDataBinder binds request parameters to JavaBean objects.
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {

    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Replace all empty strings with null.

  }

  /**
   * Flash messages are temporary data used for user notifications or storing form input. They are stored in a session and vanish as soon as they are retrieved.
   * Flash messages in Spring are created as flash attributes using RedirectAttributes's addFlashAttribute(). They are used in conjunction with RedirectView.
   */
  private void addSuccessMessage(String assignment, List<Provider> providerList, RedirectAttributes redirectAttributes){

    if(providerList.size() > 0 ){
      String successMessage = String.format("Assignments to %s successful!", assignment);
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS, successMessage);
    }
    else{
      String successMessage = String.format("Assignments for %s cleared!", assignment);
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS, successMessage);
    }

  }

  private void addErrorMessage(String assignment, String responseMessage, RedirectAttributes redirectAttributes){

    String errorMessage = String.format("Assignment to %s failed. Error message: %s", assignment, responseMessage);
    redirectAttributes.addFlashAttribute(MESSAGE_ERROR, errorMessage);

  }

  private CareTeamPatientAssignment careTeamPatientAssignment() {
    CareTeamPatientAssignment careTeamPatientAssignment = new CareTeamPatientAssignment();
    for (int i = 0; i < 3; i++) {
      careTeamPatientAssignment.addProvider(new Provider());
    }
    return careTeamPatientAssignment;
  }

  private CareTeamLocationAssignment careTeamLocationAssignment() {
    CareTeamLocationAssignment careTeamLocationAssignment = new CareTeamLocationAssignment();
    for (int i = 0; i < 3; i++) {
      careTeamLocationAssignment.addProvider(new Provider());
    }
    return careTeamLocationAssignment;
  }

}
