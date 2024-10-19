package com.example.shedu.payload.req;

import com.example.shedu.entity.enums.BookingStatus;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOrders {
    private Long serviceId;
    private Long userId;
    private Timestamp bookingDaytime;
    private Long duration;
    private BookingStatus status;
    private String special;
}


