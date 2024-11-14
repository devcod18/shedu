package com.example.shedu.payload.res;

import com.example.shedu.entity.OfferType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResOffer {
    private Long id;
    private Long barbershopId;
    private List<OfferType> offerTypes;
    private Double prise;
    private String  info;
}