package com.project.pushup.service;

import com.project.pushup.dto.UserCreationDTO;
import com.project.pushup.entity.User;
import com.project.pushup.entity.UserRoles;
import com.project.pushup.exception.ResourceNotFoundException;
import com.project.pushup.exception.UsernameAlreadyExistsException;
import com.project.pushup.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCreationDTO userCreationDTO;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup test data
        userCreationDTO = new UserCreationDTO("testuser", "Password1@");
        
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRole(UserRoles.ROLE_USER);
    }

    @Test
    void should_register_user_successfully() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User registeredUser = userService.registerUser(userCreationDTO);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals(UserRoles.ROLE_USER, registeredUser.getRole());
        
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).encode("Password1@");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void should_throw_exception_when_username_already_exists() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.registerUser(userCreationDTO);
        });
        
        verify(userRepository).findByUsername("testuser");
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void should_load_user_by_username_successfully() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void should_throw_exception_when_username_not_found() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistentuser");
        });
        
        verify(userRepository).findByUsername("nonexistentuser");
    }

    @Test
    void should_get_all_users() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setPassword("encodedPassword2");
        user2.setRole(UserRoles.ROLE_USER);
        
        List<User> userList = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals("testuser2", result.get(1).getUsername());
        
        verify(userRepository).findAll();
    }

    @Test
    void should_find_user_by_username() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Act
        User result = userService.findUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void should_throw_exception_when_finding_nonexistent_username() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserByUsername("nonexistentuser");
        });
        
        verify(userRepository).findByUsername("nonexistentuser");
    }

    @Test
    void should_get_user_by_id() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        
        verify(userRepository).findById(1L);
    }

    @Test
    void should_return_empty_when_user_id_not_found() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUserById(999L);

        // Assert
        assertFalse(result.isPresent());
        
        verify(userRepository).findById(999L);
    }
}