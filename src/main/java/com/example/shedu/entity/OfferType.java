package com.example.shedu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class OfferType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title; // Masalan, "soch olish", "soqol olish"

    @OneToMany(mappedBy = "offerType")
    private Set<Offer> offers = new HashSet<>();
}
