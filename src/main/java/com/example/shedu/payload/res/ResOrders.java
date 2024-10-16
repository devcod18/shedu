package com.example.shedu.payload.res;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResOrders {
    private Long serviceId;
    private Long userId;
    private Timestamp createdAt;
    private Long barbershopId;
    private Timestamp bookingDaytime;
    private Long duration;
    private String status;
    private String special;
}
