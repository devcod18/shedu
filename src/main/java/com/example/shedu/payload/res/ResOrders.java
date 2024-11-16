package com.example.shedu.payload.res;

import com.example.shedu.entity.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResOrders {
    private Long offerId;
    private Long userId;
    private LocalDateTime createdAt;
    private Long barbershopId;
    private LocalDate bookingDay;
    private LocalTime start;
    private LocalTime end;
    private BookingStatus status;
}
