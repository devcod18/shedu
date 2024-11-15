package com.example.shedu.repository;


import com.example.shedu.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface OffersRepository extends JpaRepository<Offer, Long> {
  @Query("select o from Offer o where o.id = ?1 and o.isDeleted = ?2")
  Optional<Offer> findByIdAndDeletedIs(Long id, boolean deleted);
  @Query("select o from Offer o where o.barberShop.id = ?1 and o.isDeleted = ?2")
  Page<Offer> findAllByBarberShopIdAndDeletedIs(Long id, boolean deleted, Pageable pageable);
  @Query("select o from Offer o where o.isDeleted = ?1")
  Page<Offer> findAllByDeleted(boolean deleted, Pageable pageable);
  @Query("select o from Offer o where o.offerType.id = ?1 and o.isDeleted = ?2")
  Page<Offer> findAllByOfferTypeIdAndDeletedIs(Long id, boolean deleted, Pageable pageable);
}