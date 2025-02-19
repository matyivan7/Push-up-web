package com.project.pushup.controller;

import com.project.pushup.dto.PushUpSessionCreationDTO;
import com.project.pushup.dto.PushUpSessionOverviewDTO;
import com.project.pushup.entity.PushUpSession;
import com.project.pushup.service.PushUpSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/new-session")
    public ResponseEntity<PushUpSession> createPushUpSession(@RequestBody PushUpSessionCreationDTO pushUpSessionCreationDTO) {
        log.info("Create session endpoint reached");

        PushUpSession savedSession = pushUpSessionService.createPushUpSession(pushUpSessionCreationDTO);
        return ResponseEntity.ok(savedSession);
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<PushUpSession>> getSessionsForCurrentUser() {
        log.info("Get Sessions for current user endpoint reached");

        List<PushUpSession> sessions = pushUpSessionService.getSessionForCurrentUser();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/all-sessions")
    public ResponseEntity<List<PushUpSession>> getAllPushUpSessions() {
        log.info("Get all push up sessions endpoint reached");

        List<PushUpSession> allSessions = pushUpSessionService.getAllSessions();
        return ResponseEntity.ok(allSessions);
    }

    @GetMapping
    public ResponseEntity<PushUpSessionOverviewDTO> getPushUpSessionOverview() {
        log.info("Get push up session overview endpoint reached");

        PushUpSessionOverviewDTO pushUpSessionOverviewDTO = pushUpSessionService.getPushUpSessionOverview();
        return ResponseEntity.ok(pushUpSessionOverviewDTO);
    }

//    @GetMapping("/summary")
//    public ResponseEntity<List<Map<String, Object>>> getDailyTotal() {
//        log.info("Get daily total sessions endpoint reached");
//
//        List<Object[]> dailyTotals = pushUpSessionService.getDailyTotals();
//
//        List<Map<String, Object>> response = dailyTotals.stream().map(record -> {
//            Map<String, Object> dailyRecord = new HashMap<>();
//            dailyRecord.put("date", record[0]);
//            dailyRecord.put("total", record[1]);
//            return dailyRecord;
//        }).toList();
//
//        return ResponseEntity.ok(response);
//    }

}
