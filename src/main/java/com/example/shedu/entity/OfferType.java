package com.example.shedu.entity;


import com.example.shedu.entity.enums.ServiceType;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqOfferType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class OfferType  {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title; // Masalan, "soch olish", "soqol olish"

    @OneToMany(mappedBy = "offerType")
    private Set<Offer> offers = new HashSet<>();
    @CreatedDate
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDate date;
}



