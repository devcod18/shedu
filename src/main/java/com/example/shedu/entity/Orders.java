package com.example.shedu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Offers offers;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private Timestamp createdAt;

    @ManyToOne(optional = false)
    private Barbershop barbershop;

    @Column(nullable = false)
    private Timestamp bookingDaytime;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String special;
}