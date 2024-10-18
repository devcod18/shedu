package com.example.shedu.entity;

import com.example.shedu.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    private Barbershop barbershop;

    @Column(nullable = false)
    private Timestamp bookingDaytime;

    @Column(nullable = false)
    private Long duration;

    @Enumerated
    private BookingStatus status;

    @Column(nullable = false)
    private String special;
}