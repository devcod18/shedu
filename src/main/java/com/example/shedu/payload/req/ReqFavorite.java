package com.example.shedu.payload.req;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqFavorite {
    private Long userId;
    private Long barberId;
    private Long barbershopId;

}


