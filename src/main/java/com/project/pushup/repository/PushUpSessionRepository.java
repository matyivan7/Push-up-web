package com.project.pushup.repository;

import com.project.pushup.entity.PushUpSession;
import com.project.pushup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushUpSessionRepository extends JpaRepository<PushUpSession, Long> {

    List<PushUpSession> findByUser(User user);
    List<PushUpSession> findAllByUser(User user);

//    @Query("SELECT FUNCTION('DATE', p.timestamp) as date, SUM(p.count) as total FROM PushUpSession p WHERE p.user = :user AND p.timestamp >= :start GROUP BY FUNCTION('DATE', p.timestamp)")
//    List<Object[]> findDailyTotals(@Param("user") User user, @Param("start") LocalDateTime start);
}
