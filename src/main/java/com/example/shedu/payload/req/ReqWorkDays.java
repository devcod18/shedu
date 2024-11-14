package com.example.shedu.payload.req;




import lombok.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqWorkDays {
    private List<Integer> dayOfWeekId;
    @DateTimeFormat(pattern = "HH:mm")
    private String openTime;
    @DateTimeFormat(pattern = "HH:mm")
    private String closeTime;
}