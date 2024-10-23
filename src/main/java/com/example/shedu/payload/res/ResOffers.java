package com.example.shedu.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResOffers {
    private Long id;
    private Long barbershopId;
    private String title;
    private String info;
    private Double price;
    private Long duration;
    private boolean deleted;
}