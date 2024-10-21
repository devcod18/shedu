package com.example.shedu.payload.req;

import com.example.shedu.entity.enums.BookingStatus;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOrders {
    private Long serviceId;
    private LocalDate bookingDay;
    private LocalTime bookingTime;
    private Long duration;
    private Long barbershopId;
}


