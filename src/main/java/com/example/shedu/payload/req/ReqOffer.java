package com.example.shedu.payload.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqOffer {
    @Min(value = 1)
    private Double price;

    @NotBlank(message = "Xizmat haqida malumot kiritish majburiy")
    private String info;

    @NotNull
    private Long barberShopId;// `BarberShop`ning ID'si

    @NotNull
    private Long offerTypeId;// `OfferType`ning ID'si
}