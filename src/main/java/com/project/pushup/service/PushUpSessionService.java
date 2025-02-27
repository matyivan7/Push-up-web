package com.project.pushup.service;

import com.project.pushup.dto.PushUpSessionCreationDTO;
import com.project.pushup.dto.PushUpSessionOverviewDTO;
import com.project.pushup.dto.model.AllPushUpSessionModel;
import com.project.pushup.dto.model.UserPushUpSessionModel;
import com.project.pushup.entity.PushUpSession;
import com.project.pushup.entity.User;
import com.project.pushup.repository.PushUpSessionRepository;
import com.project.pushup.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PushUpSessionService {

    private final PushUpSessionRepository pushUpSessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public PushUpSessionService(PushUpSessionRepository pushUpSessionRepository, UserRepository userRepository) {
        this.pushUpSessionRepository = pushUpSessionRepository;
        this.userRepository = userRepository;
    }

    public PushUpSession createPushUpSession(PushUpSessionCreationDTO pushUpSessionCreationDTO) {
        log.info("Create push up session is called");

        User user = getUser();

        PushUpSession session = new PushUpSession(pushUpSessionCreationDTO);

        session.setUser(user);
        return pushUpSessionRepository.save(session);
    }

    public List<PushUpSession> getSessionForCurrentUser() {
        log.info("Get session for current user is called");

        User user = getUser();

        return pushUpSessionRepository.findByUser(user);
    }

    public List<PushUpSession> getAllSessions() {
        log.info("Get all push up sessions method is called");

        return pushUpSessionRepository.findAll();
    }

    public PushUpSessionOverviewDTO getPushUpSessionOverview() {
        log.info("Get push up overview method is called");

        User user = getUser();

        List<UserPushUpSessionModel> userPushUpSessionModels = getUserPushUpSessionModels(user);

        List<AllPushUpSessionModel> allPushUpSessionModels = getAllPushUpSessionModels();

        return new PushUpSessionOverviewDTO(userPushUpSessionModels, allPushUpSessionModels);
    }


    private List<AllPushUpSessionModel> getAllPushUpSessionModels() {
        return pushUpSessionRepository.findAll().stream()
            .sorted((p1, p2) -> p2.getTimeStamp().compareTo(p1.getTimeStamp()))
            .map(pushUpSession -> new AllPushUpSessionModel(
                pushUpSession.getPushUpCount(),
                pushUpSession.getComment(),
                pushUpSession.getTimeStamp(),
                pushUpSession.getUser()
            ))
            .limit(10)
            .toList();
    }

    private List<UserPushUpSessionModel> getUserPushUpSessionModels(User user) {
        return Optional.ofNullable(pushUpSessionRepository.findAllByUser(user))
            .orElse(Collections.emptyList())
            .stream()
            .map(pushUpSession -> new UserPushUpSessionModel(
                pushUpSession.getPushUpCount(),
                pushUpSession.getComment(),
                pushUpSession.getTimeStamp()
            ))
            .toList();
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User with the given username: " + username + " can not be found"));
    }

}
