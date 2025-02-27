package com.project.pushup.repository;

import com.project.pushup.entity.PushUpSession;
import com.project.pushup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushUpSessionRepository extends JpaRepository<PushUpSession, Long> {

    List<PushUpSession> findByUser(User user);
    List<PushUpSession> findAllByUser(User user);

}
