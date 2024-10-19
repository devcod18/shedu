package com.example.shedu.payload.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqFeedback {
    @Size(min = 1, max = 5)
    private int rating;
    @NotBlank
    private String comment;
    private LocalDateTime createdAt;
    private Long barbershopId;
}
