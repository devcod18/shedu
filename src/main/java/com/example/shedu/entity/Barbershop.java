package com.example.shedu.entity;

import com.example.shedu.entity.enums.BarbershopRegion;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barbershop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private LocalDate date;

    @OneToOne(cascade = CascadeType.ALL)
    private User owner;

    private boolean isActive;

    @Column(nullable = false)
    private String email;

    @Column(precision = 10, scale = 7, nullable = false)
    private Double latitude;

    @Column(precision = 10, scale = 7, nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private BarbershopRegion region;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String homeNumber;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private File barbershopPic;
}