package com.project.pushup.entity;

import com.project.pushup.dto.PushUpSessionCreationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushUpSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer pushUpCount;

    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timeStamp;

    public PushUpSession(PushUpSessionCreationDTO pushUpSessionCreationDTO) {
        this.pushUpCount = pushUpSessionCreationDTO.getPushUpCount();
        this.comment = pushUpSessionCreationDTO.getComment();
        this.timeStamp = pushUpSessionCreationDTO.getTimeStamp();
    }
}
