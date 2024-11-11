
package com.example.shedu.entity;

import com.example.shedu.entity.enums.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Barbershop barbershop;

    @Column(nullable = false, unique = true)
    private String title;
    @ManyToMany
    private List<OfferType> offerTypes;
    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Long duration;

    private boolean deleted;
}