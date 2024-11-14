package com.example.shedu.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationDTO(
        Long id,
        String title,
        String content,
        boolean read,
        LocalDateTime create,
        Long userId,
        Long fileId
) {
}


