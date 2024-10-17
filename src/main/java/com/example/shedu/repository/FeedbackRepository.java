package com.example.shedu.repository;


import com.example.shedu.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByBarbershopId(Long barbershopId, Pageable pageable);

    Page<Feedback> findByUserId(Long barberId, Pageable pageable);
}










