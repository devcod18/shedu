package com.example.shedu.payload.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOffers {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Info cannot be blank")
    private String info;
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;
    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be greater than or equal to 1")
    private Long duration;
    @NotNull(message = "Offer types cannot be null")
    private List<Long> offerTypes;

}