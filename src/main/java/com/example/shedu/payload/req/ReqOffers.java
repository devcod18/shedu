package com.example.shedu.payload.req;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOffers {
    private String title;
    private String info;
    private Double price;
    private Long duration;
}