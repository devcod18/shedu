package com.example.shedu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private User user;

    private Long barbershopId;
}
