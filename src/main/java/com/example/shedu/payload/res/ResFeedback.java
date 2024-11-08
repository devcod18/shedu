package com.example.shedu.payload.res;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResFeedback {
    private Long id;
    @Min(1)
    @Max(5)
    private int rating;
    @NotBlank
    private String comment;
    private LocalDateTime createdAt;
    private Long barbershopId;
    private Long userId;
    private boolean deleted;
}