package com.example.shedu.payload.res;

import com.example.shedu.entity.enums.BookingStatus;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalDate bookingDay;
    private LocalTime bookingTime;
    private Long duration;
    private BookingStatus status;
}


