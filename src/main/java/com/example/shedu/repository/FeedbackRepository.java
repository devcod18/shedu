package com.example.shedu.repository;


import com.example.shedu.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByUserId(Long barberId, Pageable pageable);

    Page<Feedback> findByBarbershopIdAndRatingLessThanEqual(Long barbershopId, int rating, Pageable pageable);
    Page<Feedback> findByBarbershopIdAndRating(Long barbershopId, int rating, Pageable pageable);
    Page<Feedback> findByBarbershopIdAndRatingGreaterThanEqual(Long barbershopId, int rating, Pageable pageable);
    Page<Feedback> findByBarbershopId(Long barbershopId, Pageable pageable);


}












