package com.project.pushup.controller;

import com.project.pushup.dto.PushUpUserDetailsDTO;
import com.project.pushup.dto.UserCreationDTO;
import com.project.pushup.entity.User;
import com.project.pushup.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/push-up")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("permitAll")
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserCreationDTO user) {
        log.info("Registration endpoint reached");

        try {
            User userToRegister = userService.registerUser(user);
            return ResponseEntity.ok(userToRegister);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("permitAll")
    @GetMapping("/login")
    public ResponseEntity<PushUpUserDetailsDTO> login() {
        log.info("Login endpoint reached");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof UserDetails loggedInUser) {
            PushUpUserDetailsDTO loggedInUserDetails = new PushUpUserDetailsDTO();
            loggedInUserDetails.setUsername(loggedInUser.getUsername());
            loggedInUserDetails.setRoles(loggedInUser.getAuthorities().stream().map(Object::toString).toList());

            return new ResponseEntity<>(loggedInUserDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Get all users endpoint reached");

        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Get user by id endpoint reached");

        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}
