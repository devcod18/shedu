package com.example.shedu.payload.req;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOffers {
    private Long barbershopId;
    @Column(unique = true)
    private String title;
    private String info;
    private Double price;
    private Long duration;
}