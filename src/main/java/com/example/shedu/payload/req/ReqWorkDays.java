package com.example.shedu.payload.req;



import com.example.shedu.entity.Days;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
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