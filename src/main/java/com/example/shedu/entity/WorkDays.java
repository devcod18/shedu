package com.example.shedu.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String open;
    private String close;
    @OneToMany
    private List<Days> daysList;
}
