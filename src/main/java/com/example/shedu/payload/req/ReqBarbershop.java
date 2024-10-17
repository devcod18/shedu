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
    private Long id;
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Info cannot be empty")
    private String info;
    private Long owner;
    @Pattern(regexp = "\\+\\d{3}\\d{2}\\d{3}\\d{2}\\d{2}", message = "Phone number is not valid")
    private String homeNumber;
    @NotBlank(message = "Address cannot be empty")
    private Double lat;
    private Double lng;
    @NotBlank(message = "Region cannot be empty")
    private String region;
    private String email;
    private Long file_id;


}
