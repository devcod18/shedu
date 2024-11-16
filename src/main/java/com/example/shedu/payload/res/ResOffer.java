package com.example.shedu.payload.res;


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
    private String info;
    private Double price;// `BarberShop`ning nomi
    private String offerTypeTitle; // `OfferType`ning nomi (masalan, "Soch olish")
    private boolean isDeleted; // status ko'rsatkich
}