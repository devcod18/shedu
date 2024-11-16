package com.example.shedu.entity;



import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Barbershop barberShop;
    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String info;
    @ManyToOne
    private OfferType offerType;
    @Column(nullable = false)
    private boolean Deleted;

}