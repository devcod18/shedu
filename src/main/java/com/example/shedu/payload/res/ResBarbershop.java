package com.example.shedu.payload.res;

import com.example.shedu.entity.Offer;
import com.example.shedu.entity.enums.BarbershopRegion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResBarbershop {
    private Long id;
    private String title;
    private String info;
    private String email;
    private Long owner;
    private Double lat;
    private Double lng;
    private Long file_id;
    private BarbershopRegion region;
    private String address;
    private String phone;
    private List<ResUser> barberList;
    private Set<Offer> offerList;
    //<ResOffer>
}//ResBarbershop
