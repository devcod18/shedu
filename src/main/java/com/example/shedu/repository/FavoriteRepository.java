package com.example.shedu.repository;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Favorite;
import com.example.shedu.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT u FROM Favorite u WHERE u.isDeleted = false AND u.id = :id")
    Optional<Favorite> findActiveById(@Param("id") Long id);

    boolean existsByBarberAndBarbershop(User barber, Barbershop barbershop);

    @Query("SELECT u FROM Favorite u WHERE u.isDeleted = false ORDER BY u.date DESC")
    Page<Favorite> findAllActiveSorted(Pageable pageable);
}


