package com.example.shedu.payload.req;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqFeedback {
    private Long id;
    private int rating;
    private String comment;
    private LocalDateTime date;
    private Long userId;
    private Long barbershopId;
}
