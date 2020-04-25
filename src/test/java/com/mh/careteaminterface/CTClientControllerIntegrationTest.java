package com.mh.careteaminterface;

import com.mh.careteaminterface.service.CTClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CTClientControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CTClientService ctClientService;

  @Test
  public void testHome() throws Exception {
    mockMvc.perform(get("/ctassignments"))
        .andExpect(status().isOk())
        .andExpect(view().name("home"))
        .andExpect(content().string( containsString("Try Out the Care Team Interface Module!")));
  }

  @Test
  public void testAccessToken() throws Exception {
    mockMvc.perform(get("/ctassignments/token"))
        .andExpect(status().isOk())
        .andExpect(view().name("access-token"))
        .andExpect(content().string( containsString("Get Access Token!")));
  }

  @Test
  public void testLocationAssignment() throws Exception {
    mockMvc.perform(get("/ctassignments/location"))
        .andExpect(status().isOk())
        .andExpect(view().name("location-assignment"))
        .andExpect(content().string( containsString("Make Location Assignments!")));
  }

  @Test
  public void testPatientAssignment() throws Exception {
    mockMvc.perform(get("/ctassignments/patient"))
        .andExpect(status().isOk())
        .andExpect(view().name("patient-assignment"))
        .andExpect(content().string( containsString("Make Patient Assignments!")));
  }

  //@Test
  public void testPatientCareteam() throws Exception {
    mockMvc.perform(get("/ctassignments/careteam"))
        .andExpect(status().isOk())
        .andExpect(view().name("patient-careteam"))
        .andExpect(content().string( containsString("View Patient Care Team!")));
  }

}
