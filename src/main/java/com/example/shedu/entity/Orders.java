package com.example.shedu.entity;

import com.example.shedu.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @ManyToOne
    private Offer offers;

    @ManyToOne
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private Barbershop barbershop;

    private LocalDate bookingDay;

    private LocalTime startBooking;

    private LocalTime endBooking;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
