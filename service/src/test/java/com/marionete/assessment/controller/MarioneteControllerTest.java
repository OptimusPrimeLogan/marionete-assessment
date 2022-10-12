package com.marionete.assessment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marionete.assessment.exception.ControllerAdvisor;
import com.marionete.assessment.exception.TokenException;
import com.marionete.assessment.model.UserCredential;
import com.marionete.assessment.service.MarioneteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MarioneteControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    MarioneteController marioneteController;

    @Mock
    MarioneteService marioneteService;

    private String validRequestJSON, invalidRequestJSON;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(this.marioneteController)
                .setControllerAdvice(ControllerAdvisor.class)
                .build();

        lenient().when(marioneteService.getUserAccountDetails(Mockito.any(UserCredential.class)))
                .thenReturn(Mono.empty());
        UserCredential userCredential = new UserCredential();
        userCredential.setUsername("Test");
        userCredential.setPassword("Test");

        validRequestJSON = new ObjectMapper().writeValueAsString(userCredential);

        userCredential.setPassword("");

        invalidRequestJSON= new ObjectMapper().writeValueAsString(userCredential);

    }

    @Test
    void shouldGetUserAccountDetails() throws Exception {

        mockMvc.perform(post("/marionete/useraccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetUserAccountDetails_WhenInvalidData() throws Exception {

        mockMvc.perform(post("/marionete/useraccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotGetUserAccountDetails_TokenException() throws Exception {

        when(marioneteService.getUserAccountDetails(Mockito.any(UserCredential.class)))
                .thenThrow(new TokenException("Test"));

        mockMvc.perform(post("/marionete/useraccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldNotGetUserAccountDetails_AnyException() throws Exception {

        when(marioneteService.getUserAccountDetails(Mockito.any(UserCredential.class)))
                .thenThrow(new RuntimeException("Test"));

        mockMvc.perform(post("/marionete/useraccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJSON)
                )
                .andExpect(status().isInternalServerError());
    }


}
