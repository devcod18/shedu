package com.example.shedu.payload.req;

import com.example.shedu.entity.enums.ServiceType;
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
public class ReqOfferType {
    @NotBlank(message = "Title cannot be blank")
    private String title;
}