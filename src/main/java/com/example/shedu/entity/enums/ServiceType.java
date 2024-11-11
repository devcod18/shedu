package com.example.shedu.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ServiceType {
    HAIR_CUT("Soch olish"),
    HAIR_COLORING("Soch bo'yash"),
    HAIR_STYLING("Soch turmaklash"),
    BEARD_TRIMMING("Soqol olish"),
    SHAVING("Soqol olish"),
    FACIAL("Yuzni parvarishlash"),
    MANICURE("Manikyur"),
    PEDICURE("Pedikyur"),
    MASSAGE("Massaj"),
    HAIR_TREATMENT("Soch davolash"),
    HAIR_REMOVAL("Soch olib tashlash");

    private final String name;

}