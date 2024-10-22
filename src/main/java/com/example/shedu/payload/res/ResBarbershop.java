package com.example.shedu.payload.res;

import com.example.shedu.entity.enums.BarbershopRegion;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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


}
