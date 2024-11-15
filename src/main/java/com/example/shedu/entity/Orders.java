package com.example.shedu.entity;

import com.example.shedu.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    private Offer offers;

    @ManyToOne(optional = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    private Barbershop barbershop;

    @Column(nullable = false)
    private LocalDateTime bookingDaytime;

    @Column(nullable = false)
    private Long duration;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
