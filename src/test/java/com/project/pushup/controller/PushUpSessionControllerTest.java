package com.project.pushup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.pushup.dto.PushUpSessionCreationDTO;
import com.project.pushup.dto.PushUpSessionOverviewDTO;
import com.project.pushup.dto.model.AllPushUpSessionModel;
import com.project.pushup.dto.model.UserPushUpSessionModel;
import com.project.pushup.entity.PushUpSession;
import com.project.pushup.entity.User;
import com.project.pushup.entity.UserRoles;
import com.project.pushup.service.PushUpSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PushUpSessionController.class)
@Import(PushUpSessionControllerTest.TestConfig.class)
public class PushUpSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PushUpSessionService pushUpSessionService;

    static class TestConfig {
        @Bean
        public PushUpSessionService pushUpSessionService() {
            return mock(PushUpSessionService.class);
        }
    }

    private User testUser;
    private PushUpSession testSession;
    private PushUpSessionCreationDTO testSessionDTO;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole(UserRoles.ROLE_USER);

        testSessionDTO = new PushUpSessionCreationDTO(10, "Test comment", now);

        testSession = new PushUpSession();
        testSession.setId(1L);
        testSession.setUser(testUser);
        testSession.setPushUpCount(10);
        testSession.setComment("Test comment");
        testSession.setTimeStamp(now);
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_create_push_up_session_successfully() throws Exception {
        // Arrange
        when(pushUpSessionService.createPushUpSession(any(PushUpSessionCreationDTO.class))).thenReturn(testSession);

        // Act & Assert
        mockMvc.perform(post("/push-up/new-session")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSessionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.pushUpCount", is(10)))
                .andExpect(jsonPath("$.comment", is("Test comment")));

        verify(pushUpSessionService).createPushUpSession(any(PushUpSessionCreationDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_get_sessions_for_current_user() throws Exception {
        // Arrange
        List<PushUpSession> sessions = Collections.singletonList(testSession);
        when(pushUpSessionService.getSessionForCurrentUser()).thenReturn(sessions);

        // Act & Assert
        mockMvc.perform(get("/push-up/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].pushUpCount", is(10)))
                .andExpect(jsonPath("$[0].comment", is("Test comment")));

        verify(pushUpSessionService).getSessionForCurrentUser();
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_get_all_sessions() throws Exception {
        // Arrange
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("testuser2");

        PushUpSession testSession2 = new PushUpSession();
        testSession2.setId(2L);
        testSession2.setUser(testUser2);
        testSession2.setPushUpCount(20);
        testSession2.setComment("Another comment");
        testSession2.setTimeStamp(now.plusDays(1));

        List<PushUpSession> allSessions = Arrays.asList(testSession, testSession2);
        when(pushUpSessionService.getAllSessions()).thenReturn(allSessions);

        // Act & Assert
        mockMvc.perform(get("/push-up/all-sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].pushUpCount", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].pushUpCount", is(20)));

        verify(pushUpSessionService).getAllSessions();
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_get_push_up_session_overview() throws Exception {
        // Arrange
        List<UserPushUpSessionModel> userModels = Collections.singletonList(
                new UserPushUpSessionModel(10, "Test comment", now));

        List<AllPushUpSessionModel> allModels = Collections.singletonList(
                new AllPushUpSessionModel(10, "Test comment", now, testUser));

        PushUpSessionOverviewDTO overviewDTO = new PushUpSessionOverviewDTO(userModels, allModels);

        when(pushUpSessionService.getPushUpSessionOverview()).thenReturn(overviewDTO);

        // Act & Assert
        mockMvc.perform(get("/push-up"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userPushUpSessionModels", hasSize(1)))
                .andExpect(jsonPath("$.userPushUpSessionModels[0].pushUpCount", is(10)))
                .andExpect(jsonPath("$.allPushUpSessionModels", hasSize(1)))
                .andExpect(jsonPath("$.allPushUpSessionModels[0].pushUpCount", is(10)));

        verify(pushUpSessionService).getPushUpSessionOverview();
    }
}
