package com.example.shedu.repository;

import com.example.shedu.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f FROM Feedback f WHERE f.barbershopId = ?1 ORDER BY f.id DESC")
    Page<Feedback> findByBarbershopId(Long barbershopId, Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE f.barbershopId = ?1 AND f.rating <= ?2 ORDER BY f.id DESC")
    Page<Feedback> findByBarbershopIdAndRatingLessThanEqual(Long barbershopId, int rating, Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE f.barbershopId = ?1 AND f.rating = ?2 ORDER BY f.id DESC")
    Page<Feedback> findByBarbershopIdAndRating(Long barbershopId, int rating, Pageable pageable);

    @Query("SELECT f FROM Feedback f WHERE f.barbershopId = ?1 AND f.rating >= ?2 ORDER BY f.id DESC")
    Page<Feedback> findByBarbershopIdAndRatingGreaterThanEqual(Long barbershopId, int rating, Pageable pageable);
}
