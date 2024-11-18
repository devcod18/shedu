package com.example.shedu.payload;

import com.example.shedu.entity.Offer;
import com.example.shedu.entity.enums.BarbershopRegion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link Offer}
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class OfferDto implements Serializable {
    private final Long id;
    private final Long barberShopId;
    private final String barberShopTitle;
    private final String barberShopPhoneNumber;
    private final BarbershopRegion barberShopRegion;
    private final Double price;
    private final String info;
    private final Long offerTypeId;
    private final String offerTypeTitle;
}