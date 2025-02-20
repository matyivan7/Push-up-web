package com.project.pushup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushUpSessionCreationDTO {

    private Integer pushUpCount;

    private String comment;

    private LocalDateTime timeStamp;
}
