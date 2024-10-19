package com.example.shedu.entity;

import com.example.shedu.entity.enums.WeekDays;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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
   @OneToOne
    private Barbershop barbershopId;
    private Timestamp open;
    private Timestamp close;


    @OneToMany
    private List<Days> daysList;
}
