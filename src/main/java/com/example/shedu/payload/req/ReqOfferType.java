package com.example.shedu.payload.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Bag;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqOfferType {
    @NotBlank
    private String offerType;
}
