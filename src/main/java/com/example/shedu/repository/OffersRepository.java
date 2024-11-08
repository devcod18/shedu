package com.example.shedu.repository;


import com.example.shedu.entity.Favorite;
import com.example.shedu.entity.Offers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OffersRepository extends JpaRepository<Offers, Long> {
    @Query("SELECT u FROM Offers u WHERE u.deleted = false ORDER BY u.id DESC")
    Page<Offers> findAllActiveSorted(Pageable pageable);
    boolean existsByBarbershopIdAndTitle(Long barbershopId, String title);
    Offers findByBarbershopId(Long id);
    List<Offers> findAllByBarbershopId(Long barbershopId);

}