package com.project.pushup.service;

import com.project.pushup.entity.PushUpSession;
import com.project.pushup.entity.User;
import com.project.pushup.repository.PushUpSessionRepository;
import com.project.pushup.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PushUpSessionService {

    private static final Logger log = LogManager.getLogger(PushUpSessionService.class);
    private final PushUpSessionRepository pushUpSessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public PushUpSessionService(PushUpSessionRepository pushUpSessionRepository, UserRepository userRepository) {
        this.pushUpSessionRepository = pushUpSessionRepository;
        this.userRepository = userRepository;
    }

    public PushUpSession createPushUpSession(PushUpSession session) {
        log.info("Create push up session is called");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with the given username: " + username + " can not be found"));
        session.setUser(user);
        return pushUpSessionRepository.save(session);
    }

    public List<PushUpSession> getSessionForCurrentUser() {
        log.info("Get session for current user is called");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with the given username: " + username + " can not be found"));

        return pushUpSessionRepository.findByUser(user);
    }

    public List<PushUpSession> getAllSessions() {
        log.info("Get all push up sessions method is called");

        return pushUpSessionRepository.findAll();
    }

//    public List<Object[]> getDailyTotals() {
//        log.info("Get daily total push ups method is called");
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with the given username: " + username + " can not be found"));
//        LocalDateTime start = LocalDateTime.now().minusDays(30);
//        return pushUpSessionRepository.findDailyTotals(user, start);
//    }
}
