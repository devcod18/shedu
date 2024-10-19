package com.example.shedu.payload.req;

import com.example.shedu.entity.Days;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqWorkDays {
    private Long barbershopId;
    private Timestamp open;
    private Timestamp close;
    private List<Days> daysList;
}
