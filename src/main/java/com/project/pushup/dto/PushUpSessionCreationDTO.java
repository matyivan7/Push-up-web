package com.project.pushup.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushUpSessionCreationDTO {

    @NotNull(message = "Push-up count is required")
    @Min(value = 1, message = "Push-up count must be at least 1")
    private Integer pushUpCount;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String comment;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timeStamp;
}
