
package com.example.shedu.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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
    @JoinColumn( nullable = false)
    private Barbershop barbershop;

    @ManyToMany
    private List<OfferType> offerTypes;
    private String info;



    @Column(nullable = false)
    private boolean isDeleted;
}