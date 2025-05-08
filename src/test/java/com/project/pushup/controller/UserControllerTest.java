package com.project.pushup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.pushup.dto.LoginRequest;
import com.project.pushup.dto.UserCreationDTO;
import com.project.pushup.entity.User;
import com.project.pushup.entity.UserRoles;
import com.project.pushup.exception.UsernameAlreadyExistsException;
import com.project.pushup.service.JwtService;
import com.project.pushup.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.TestConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }

        @Bean
        public JwtService jwtService() {
            return mock(JwtService.class);
        }

        @Bean
        public AuthenticationManager authenticationManager() {
            return mock(AuthenticationManager.class);
        }
    }

    private UserCreationDTO userCreationDTO;
    private User user;
    private LoginRequest loginRequest;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        userCreationDTO = new UserCreationDTO("testuser", "Password1@");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRole(UserRoles.ROLE_USER);

        loginRequest = new LoginRequest("testuser", "Password1@");

        authentication = new UsernamePasswordAuthenticationToken("testuser", "Password1@");
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_register_user_successfully() throws Exception {
        // Arrange
        when(userService.registerUser(any(UserCreationDTO.class))).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/push-up/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.role", is("ROLE_USER")));

        verify(userService, atLeastOnce()).registerUser(any(UserCreationDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_return_bad_request_when_registration_fails() throws Exception {
        // Arrange
        when(userService.registerUser(any(UserCreationDTO.class)))
                .thenThrow(new UsernameAlreadyExistsException("User with the given username already exists."));

        // Act & Assert
        mockMvc.perform(post("/push-up/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreationDTO)))
                .andExpect(status().isBadRequest());

        verify(userService).registerUser(any(UserCreationDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_login_user_successfully() throws Exception {
        // Arrange
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtService.generateToken(anyString())).thenReturn("test.jwt.token");
        when(jwtService.getJwtExpirationMs()).thenReturn(3600000);

        // Act & Assert
        mockMvc.perform(post("/push-up/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Login successful")))
                .andExpect(cookie().exists("JWT_TOKEN"))
                .andExpect(cookie().value("JWT_TOKEN", "test.jwt.token"))
                .andExpect(cookie().httpOnly("JWT_TOKEN", true))
                .andExpect(cookie().secure("JWT_TOKEN", true))
                .andExpect(cookie().path("JWT_TOKEN", "/push-up"))
                .andExpect(cookie().maxAge("JWT_TOKEN", 3600000));

        verify(authenticationManager, atLeastOnce()).authenticate(any(Authentication.class));
        verify(jwtService).generateToken("testuser");
        verify(jwtService).getJwtExpirationMs();
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_return_unauthorized_when_login_fails() throws Exception {
        // Arrange
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        mockMvc.perform(post("/push-up/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));

        verify(authenticationManager).authenticate(any(Authentication.class));
        verifyNoInteractions(jwtService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_logout_user_successfully() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/push-up/logout")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"))
                .andExpect(cookie().exists("JWT_TOKEN"))
                .andExpect(cookie().value("JWT_TOKEN", nullValue()))
                .andExpect(cookie().maxAge("JWT_TOKEN", 0));
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_get_all_users() throws Exception {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setPassword("encodedPassword2");
        user2.setRole(UserRoles.ROLE_USER);

        List<User> users = Arrays.asList(user, user2);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/push-up/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("testuser")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("testuser2")));

        verify(userService).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_get_user_by_id() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(get("/push-up/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.role", is("ROLE_USER")));

        verify(userService).getUserById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void should_return_not_found_when_user_id_not_found() throws Exception {
        // Arrange
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/push-up/user/999"))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(999L);
    }
}
