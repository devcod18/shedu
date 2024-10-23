package com.example.shedu.payload.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqBarbershop {
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Info cannot be empty")
    private String info;
    @NotBlank(message = "Address cannot be empty")
    private Double lat;
    private Double lng;
    private String address;
    private Long file_id;
}