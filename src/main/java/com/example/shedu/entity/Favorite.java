package com.example.shedu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private User barber;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Barbershop barbershop;

    @CreationTimestamp
    private LocalDate date;
}
