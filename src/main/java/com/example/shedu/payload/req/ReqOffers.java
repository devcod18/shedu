package com.example.shedu.payload.req;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOffers {
    private Long id;
    private Long barbershopId;
    private String title;
    private String info;
    private Double price;
    private Long duration;
}