package com.example.shedu.repository;


import com.example.shedu.entity.Offers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OffersRepository extends JpaRepository<Offers, Long> {
    @Query("SELECT u FROM Offers u WHERE u.deleted = false ORDER BY u.id DESC")
    Page<Offers> findByDeletedIsAndOrderByIdDesc(Pageable pageable);
    boolean existsByBarbershopIdAndTitle(Long barbershopId, String title);
    Offers findByBarbershopId(Long id);
    List<Offers> findByBarbershopIdAndDeleted(Long barbershopId, boolean deleted);
   Offers findByBarbershopIdAndTitle(Long barbershopId, String title);
   @Query("SELECT u FROM Offers u WHERE u.deleted = false AND u.id = ?1")
   Offers findByIdAndDeleted(Long id);
//   Optional<Offers> findByBarbershopId(Long id, Long barbershopId);
}