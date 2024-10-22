package com.example.shedu.payload.res;

import com.example.shedu.entity.Days;
import com.example.shedu.entity.WorkDays;
import lombok.*;

import java.time.LocalTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResWorkDay {

    private Long id;
    private Long barbershopId;
    private List<Days> dayOfWeek;
    private String openTime;
    private String closeTime;
}
