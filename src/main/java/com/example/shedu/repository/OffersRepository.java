package com.example.shedu.repository;


import com.example.shedu.entity.Favorite;
import com.example.shedu.entity.Offers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OffersRepository extends JpaRepository<Offers, Long> {
    @Query("SELECT u FROM Offers u WHERE u.deleted = false ORDER BY u.id DESC")
    Page<Offers> findAllActiveSorted(Pageable pageable);
}