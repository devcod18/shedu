package com.example.shedu.payload.req;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOrders {
    private Long offerId;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate bookingDay;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startBooking;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endBooking;

    private Long barbershopId;
}
