package com.project.pushup.controller;

import com.project.pushup.dto.LoginRequest;
import com.project.pushup.dto.UserCreationDTO;
import com.project.pushup.entity.User;
import com.project.pushup.exception.ValidationException;
import com.project.pushup.service.JwtService;
import com.project.pushup.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/push-up")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PreAuthorize("permitAll")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserCreationDTO user, BindingResult bindingResult) {
        log.info("Registration endpoint reached");

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        try {
            User userToRegister = userService.registerUser(user);
            return ResponseEntity.ok(userToRegister);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, 
                                             BindingResult bindingResult, 
                                             HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtService.generateToken(loginRequest.getUsername());

            Cookie cookie = new Cookie("JWT_TOKEN", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/push-up");
            cookie.setMaxAge(jwtService.getJwtExpirationMs());
            cookie.setAttribute("SameSite", "None");

            response.addCookie(cookie);

            Map<String, String> result = new HashMap<>();
            result.put("message", "Login successful");

            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/push-up");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Get all users endpoint reached");

        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Get user by id endpoint reached");

        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}
