package com.example.shedu.payload.res;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResFeedback {
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private Long userId;
    private Long barbershopId;
}
