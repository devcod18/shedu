package com.example.shedu.payload;

import com.example.shedu.payload.res.ResBarbershop;
import com.example.shedu.payload.res.ResOffer;
import com.example.shedu.payload.res.ResWorkDay;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BarbershopDto {
    private ResBarbershop barbershop;
    private ResWorkDay workDay;

}