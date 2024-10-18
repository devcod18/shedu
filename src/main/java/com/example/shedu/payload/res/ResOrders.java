package com.example.shedu.payload.res;

import com.example.shedu.entity.enums.BookingStatus;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResOrders {
    private Long serviceId;
    private Long userId;
    private LocalDateTime createdAt;
    private Long barbershopId;
    private Timestamp bookingDaytime;
    private Long duration;
    private BookingStatus status;
    private String special;
}
