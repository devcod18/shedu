package com.example.shedu.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkDays {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   @OneToOne(cascade = CascadeType.MERGE)
    private Barbershop barbershopId;
    private LocalTime open;
    private LocalTime close;
    @ManyToMany
    private List<Days> daysList;
}