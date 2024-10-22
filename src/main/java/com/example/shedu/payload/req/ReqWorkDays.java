package com.example.shedu.payload.req;



import com.example.shedu.entity.Days;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqWorkDays {
    private List<Integer> dayOfWeekId;
    private String openTime;
    private String closeTime;
    private Long barbershopId;

}
