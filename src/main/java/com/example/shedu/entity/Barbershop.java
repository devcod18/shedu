package com.example.shedu.entity;

import com.example.shedu.entity.enums.BarbershopRegion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    private LocalDate date;

    @OneToOne(cascade = CascadeType.ALL)
    private User owner;

    private boolean isActive;

    @Column(nullable = false)
    private String email;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private BarbershopRegion region;

    private String address;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private File barbershopPic;
}