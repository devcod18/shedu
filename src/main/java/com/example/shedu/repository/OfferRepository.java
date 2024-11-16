package com.example.shedu.repository;

import com.example.shedu.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    Page<Offer> findAllBy(Pageable pageable);
    Offer findByBarberShopId(Long id);

}
