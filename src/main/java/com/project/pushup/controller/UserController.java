package com.project.pushup.controller;

import com.project.pushup.dto.PushUpUserDetailsDTO;
import com.project.pushup.dto.UserCreationDTO;
import com.project.pushup.entity.User;
import com.project.pushup.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

//    @PreAuthorize("permitAll")
//    @PostMapping("/login")
//    public ResponseEntity<PushUpUserDetailsDTO> login(@RequestBody PushUpUserDetailsDTO pushUpUserDetailsDTO) {
//        log.info("Login endpoint reached");
//
//        try {
//            PushUpUserDetailsDTO loggedInUserDetails = userService.loginUser(pushUpUserDetailsDTO);
//            return ResponseEntity.ok(loggedInUserDetails);
//        } catch (RuntimeException ex) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Get all users endpoint reached");

        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")

    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Get user by id endpoint reached");

        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}
