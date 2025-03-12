package com.project.pushup.service;

import com.project.pushup.dto.UserCreationDTO;
import com.project.pushup.entity.User;
import com.project.pushup.entity.UserRoles;
import com.project.pushup.exception.UsernameAlreadyExistsException;
import com.project.pushup.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserCreationDTO userCreationDTO) {
        log.info("RegisterUser method is called");

        if (userRepository.findByUsername(userCreationDTO.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("User with the given username already exists.");
        }

        User user = new User(userCreationDTO);
        user.setRole(UserRoles.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Load user by username method is called");

        User user = userRepository.findByUsername(username)
            .orElseThrow( () -> new UsernameNotFoundException("User with the given username: " + username + " can not be found"));

        String role = user.getRole().toString();

        log.info(user.getUsername(), user.getRole());

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .authorities(AuthorityUtils.createAuthorityList(role))
            .password(user.getPassword())
            .build();
    }

    public List<User> getAllUsers() {
        log.info("Get all users method is called");

        return userRepository.findAll();
    }

    public User findUserByUsername(String username) {
        log.info("Find user by username method is called");

        User user = userRepository.findByUsername(username)
            .orElseThrow( () -> new UsernameNotFoundException("User with the given username: " + username + " can not be found"));

        return user;
    }

    public Optional<User> getUserById(Long id) {
        log.info("Get user by id is called");

        return userRepository.findById(id);
    }

}
