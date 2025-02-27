package com.project.pushup.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPushUpSessionModel {

    private Integer pushUpCount;

    private String comment;

    private LocalDateTime timeStamp;

}
