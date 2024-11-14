package com.example.shedu.payload.req;

import com.example.shedu.entity.OfferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class ReqOffer {
    @NotBlank(message = "please enter information")
    private Long barberId;
    @NotBlank
    private List<OfferType> offerTypes;
    @NotNull(message = "please enter information")
    private Double prise;
    private String  info;

}
