package com.project.pushup.controller;

import com.project.pushup.dto.PushUpSessionCreationDTO;
import com.project.pushup.dto.PushUpSessionOverviewDTO;
import com.project.pushup.entity.PushUpSession;
import com.project.pushup.service.PushUpSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/push-up")
public class PushUpSessionController {

    private final PushUpSessionService pushUpSessionService;

    @Autowired
    public PushUpSessionController(PushUpSessionService pushUpSessionService) {
        this.pushUpSessionService = pushUpSessionService;
    }

    @PreAuthorize("hasRole('ROLE_USER'")
    @PostMapping("/new-session")
    public ResponseEntity<PushUpSession> createPushUpSession(@RequestBody PushUpSessionCreationDTO pushUpSessionCreationDTO) {
        log.info("Create session endpoint reached");

        PushUpSession savedSession = pushUpSessionService.createPushUpSession(pushUpSessionCreationDTO);
        return ResponseEntity.ok(savedSession);
    }

    @PreAuthorize("hasRole('ROLE_USER'")
    @GetMapping("/sessions")
    public ResponseEntity<List<PushUpSession>> getSessionsForCurrentUser() {
        log.info("Get Sessions for current user endpoint reached");

        List<PushUpSession> sessions = pushUpSessionService.getSessionForCurrentUser();
        return ResponseEntity.ok(sessions);
    }

    @PreAuthorize("hasRole('ROLE_USER'")
    @GetMapping("/all-sessions")
    public ResponseEntity<List<PushUpSession>> getAllPushUpSessions() {
        log.info("Get all push up sessions endpoint reached");

        List<PushUpSession> allSessions = pushUpSessionService.getAllSessions();
        return ResponseEntity.ok(allSessions);
    }

    @PreAuthorize("hasRole('ROLE_USER'")
    @GetMapping
    public ResponseEntity<PushUpSessionOverviewDTO> getPushUpSessionOverview() {
        log.info("Get push up session overview endpoint reached");

        PushUpSessionOverviewDTO pushUpSessionOverviewDTO = pushUpSessionService.getPushUpSessionOverview();
        return ResponseEntity.ok(pushUpSessionOverviewDTO);
    }
}
