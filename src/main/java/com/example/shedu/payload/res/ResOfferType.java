package com.example.shedu.payload.res;

import com.example.shedu.entity.Offer;
import com.example.shedu.entity.enums.ServiceType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResOfferType {
    private Long id;
    private String title;
    private LocalDateTime created;
    private LocalDate date;
}
