package com.project.pushup.dto.model;

import com.project.pushup.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllPushUpSessionModel {

    private Integer pushUpCount;

    private String comment;

    private LocalDateTime timeStamp;

    private User user;
}
