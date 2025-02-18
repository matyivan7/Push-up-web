package com.project.pushup.service;

import com.project.pushup.dto.PushUpUserDetails;
import com.project.pushup.dto.UserCreationDTO;
import com.project.pushup.entity.User;
import com.project.pushup.exception.UsernameAlreadyExistsException;
import com.project.pushup.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserCreationDTO userCreationDTO) {
        log.info("RegisterUser method is called");

        User user = new User(userCreationDTO);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("User with the given username already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public PushUpUserDetails loginUser(PushUpUserDetails pushUpUserDetails) {
        log.info("loginUser method is called");

        String username = pushUpUserDetails.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("User with the given username: " + username + " can not be found"));

        return new PushUpUserDetails(user);

    }

    public List<User> getAllUsers() {
        log.info("Get all users method is called");

        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        log.info("Get user by id is called");

        return userRepository.findById(id);
    }
}
