package com.project.pushup.service;

import com.project.pushup.dto.PushUpSessionCreationDTO;
import com.project.pushup.dto.PushUpSessionOverviewDTO;
import com.project.pushup.entity.PushUpSession;
import com.project.pushup.entity.User;
import com.project.pushup.entity.UserRoles;
import com.project.pushup.exception.ResourceNotFoundException;
import com.project.pushup.repository.PushUpSessionRepository;
import com.project.pushup.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PushUpSessionServiceTest {

    @Mock
    private PushUpSessionRepository pushUpSessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private PushUpSessionService pushUpSessionService;

    private User testUser;
    private PushUpSession testSession;
    private PushUpSessionCreationDTO testSessionDTO;
    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;

    @BeforeEach
    void setUp() {
        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setRole(UserRoles.ROLE_USER);

        LocalDateTime now = LocalDateTime.now();

        testSessionDTO = new PushUpSessionCreationDTO(10, "Test comment", now);

        testSession = new PushUpSession();
        testSession.setId(1L);
        testSession.setUser(testUser);
        testSession.setPushUpCount(10);
        testSession.setComment("Test comment");
        testSession.setTimeStamp(now);

        // Mock SecurityContextHolder
        mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("testuser");

        // Mock UserRepository
        lenient().when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Test
    void should_create_push_up_session_successfully() {
        // Arrange
        when(pushUpSessionRepository.save(any(PushUpSession.class))).thenReturn(testSession);

        // Act
        PushUpSession result = pushUpSessionService.createPushUpSession(testSessionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10, result.getPushUpCount());
        assertEquals("Test comment", result.getComment());
        assertEquals(testUser, result.getUser());

        verify(userRepository).findByUsername("testuser");
        verify(pushUpSessionRepository).save(any(PushUpSession.class));
    }

    @Test
    void should_throw_exception_when_user_not_found() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            pushUpSessionService.createPushUpSession(testSessionDTO);
        });

        verify(userRepository).findByUsername("testuser");
        verifyNoInteractions(pushUpSessionRepository);
    }

    @Test
    void should_get_sessions_for_current_user() {
        // Arrange
        List<PushUpSession> sessions = Arrays.asList(testSession);
        when(pushUpSessionRepository.findByUser(any(User.class))).thenReturn(sessions);

        // Act
        List<PushUpSession> result = pushUpSessionService.getSessionForCurrentUser();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(10, result.get(0).getPushUpCount());

        verify(userRepository).findByUsername("testuser");
        verify(pushUpSessionRepository).findByUser(testUser);
    }

    @Test
    void should_get_all_sessions() {
        // Arrange
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("testuser2");

        PushUpSession testSession2 = new PushUpSession();
        testSession2.setId(2L);
        testSession2.setUser(testUser2);
        testSession2.setPushUpCount(20);

        List<PushUpSession> allSessions = Arrays.asList(testSession, testSession2);
        when(pushUpSessionRepository.findAll()).thenReturn(allSessions);

        // Act
        List<PushUpSession> result = pushUpSessionService.getAllSessions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(pushUpSessionRepository).findAll();
    }

    @Test
    void should_get_push_up_session_overview() {
        // Arrange
        List<PushUpSession> userSessions = Collections.singletonList(testSession);
        List<PushUpSession> allSessions = Collections.singletonList(testSession);

        when(pushUpSessionRepository.findAllByUser(any(User.class))).thenReturn(userSessions);
        when(pushUpSessionRepository.findAll()).thenReturn(allSessions);

        // Act
        PushUpSessionOverviewDTO result = pushUpSessionService.getPushUpSessionOverview();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getUserPushUpSessionModels().size());
        assertEquals(1, result.getAllPushUpSessionModels().size());
        assertEquals(10, result.getUserPushUpSessionModels().get(0).getPushUpCount());
        assertEquals("Test comment", result.getUserPushUpSessionModels().get(0).getComment());
        assertEquals(10, result.getAllPushUpSessionModels().get(0).getPushUpCount());
        assertEquals("Test comment", result.getAllPushUpSessionModels().get(0).getComment());
        assertEquals(testUser, result.getAllPushUpSessionModels().get(0).getUser());

        verify(userRepository).findByUsername("testuser");
        verify(pushUpSessionRepository).findAllByUser(testUser);
        verify(pushUpSessionRepository).findAll();
    }

    @Test
    void should_return_empty_list_when_no_user_sessions() {
        // Arrange
        when(pushUpSessionRepository.findAllByUser(any(User.class))).thenReturn(null);
        when(pushUpSessionRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        PushUpSessionOverviewDTO result = pushUpSessionService.getPushUpSessionOverview();

        // Assert
        assertNotNull(result);
        assertTrue(result.getUserPushUpSessionModels().isEmpty());
        assertTrue(result.getAllPushUpSessionModels().isEmpty());

        verify(userRepository).findByUsername("testuser");
        verify(pushUpSessionRepository).findAllByUser(testUser);
        verify(pushUpSessionRepository).findAll();
    }
}
