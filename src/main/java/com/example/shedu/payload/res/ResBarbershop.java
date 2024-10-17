package com.example.shedu.payload.res;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResBarbershop {

    private String title;
    private String info;
    private String email;
    private Long owner;
    private Double lat;
    private Double lng;
    private String homeNumber;
    private Long file_id ;
    private String region;



}
