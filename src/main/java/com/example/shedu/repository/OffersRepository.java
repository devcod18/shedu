package com.example.shedu.repository;


import com.example.shedu.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OffersRepository extends JpaRepository<Offer, Long> {
  Optional<Offer> findByIdAndDeletedIs(Long id, boolean deleted);
  List<Offer> findAllByBarberShopIdAndDeletedIs(Long id, boolean deleted);
  Page<Offer> findAllByBarberShopIdAndDeletedIs(Long id, boolean deleted, Pageable pageable);
  Page<Offer> findAllByDeletedIs(boolean deleted, Pageable pageable);
  Page<Offer> findAllByOfferTypeIdAndDeletedIs(Long id, boolean deleted, Pageable pageable);
}