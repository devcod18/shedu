package com.example.shedu.payload.req;


import com.example.shedu.entity.OfferType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.NonNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqOffer {
    @Min(value = 1)
    private Double price;

    @NotBlank(message = "Xizmat nomini kiritish majburiy")
    private Integer duration;// xizmat davomiyligi (daqiqalarda)

    @NotBlank(message = "Xizmat haqida malumot kiritish majburiy")
    private String info;

    @NotNull
    private Long barberShopId;// `BarberShop`ning ID'si

    @NotNull
    private Long offerTypeId;// `OfferType`ning ID'si
}
