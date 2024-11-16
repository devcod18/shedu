package com.example.shedu.entity;



import jakarta.persistence.*;
import lombok.*;


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