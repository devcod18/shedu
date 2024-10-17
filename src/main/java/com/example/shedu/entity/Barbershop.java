package com.example.shedu.entity;

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
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7, nullable = false)
    private BigDecimal longitude;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String homeNumber;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

//    private File barbershopPic;
}