package com.example.shedu.payload.res;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResFavorite {
    private Long userId;
    private String userName;
    private Long barberId;
    private String barberName;
    private Long barbershopId;
    private String barbershopName;
    private LocalDate date;
}
