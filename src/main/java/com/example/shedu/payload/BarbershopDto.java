package com.example.shedu.payload;

import com.example.shedu.payload.res.ResBarbershop;
import com.example.shedu.payload.res.ResWorkDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BarbershopDto {
    private ResBarbershop barbershop;
    private ResWorkDay workDay;
}