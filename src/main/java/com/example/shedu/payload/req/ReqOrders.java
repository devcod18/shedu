package com.example.shedu.payload.req;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOrders {
    private Long serviceId;
    private LocalDateTime bookingDaytime;
    private Long duration;
}
