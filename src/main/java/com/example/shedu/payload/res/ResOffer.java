package com.example.shedu.payload.res;

import com.example.shedu.entity.OfferType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResOffer {
    private Long id;
    private Double price;
    private Long duration;
    private String barberShopName; // `BarberShop`ning nomi
    private String offerTypeTitle; // `OfferType`ning nomi (masalan, "Soch olish")
    private boolean isDeleted; // status ko'rsatkich
}