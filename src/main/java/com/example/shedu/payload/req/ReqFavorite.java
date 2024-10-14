package com.example.shedu.payload.req;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqFavorite {
    private Long id;
    private Long userId;
    private Long barberId;
    private Long barbershopId;
    private LocalDate date;
}
