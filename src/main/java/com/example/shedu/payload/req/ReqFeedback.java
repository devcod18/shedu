package com.example.shedu.payload.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqFeedback {
    @Min(1)
    @Max(5)
    private int rating;
    @NotBlank
    private String comment;
    private Long barbershopId;
}