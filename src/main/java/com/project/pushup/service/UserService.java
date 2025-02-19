package com.project.pushup.service;

import com.project.pushup.dto.PushUpUserDetailsDTO;
import com.project.pushup.dto.UserCreationDTO;
import com.project.pushup.entity.User;
import com.project.pushup.exception.UsernameAlreadyExistsException;
import com.project.pushup.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PushUpUserDetailsDTO loginUser(PushUpUserDetailsDTO pushUpUserDetailsDTO) {
        log.info("loginUser method is called");

        String username = pushUpUserDetailsDTO.getUsername();

        User user = userRepository.findByUsername(username).orElseThrow( () -> new UsernameNotFoundException("User with the given username: " + username + " can not be found"));

        return new PushUpUserDetailsDTO(user);

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
