package com.example.shedu.payload.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqFavorite {
    @NotBlank
    private Long barberId;
    @NotBlank
    private Long barbershopId;
}