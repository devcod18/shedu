package com.example.shedu.entity;

import com.example.shedu.entity.enums.BarbershopRegion;
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
public class Barbershop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String title;

    @Column(nullable = false)
    private String info;

    @CreationTimestamp
    private LocalDate created;

    @ManyToOne
    private User owner;

    private boolean isActive;

    @Column(nullable = false)
    private String email;

    private Double latitude;

    private Double longitude;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private BarbershopRegion region;
    @Column(nullable = false,unique = true)
    private String address;

    @ManyToOne
    private File barbershopPic;
}