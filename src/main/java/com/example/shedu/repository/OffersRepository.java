package com.example.shedu.repository;


import com.example.shedu.entity.Offer;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OffersRepository extends JpaRepository<Offer, Long> {
   @Query("select o from Offer o where o.barbershop.id = ?1 and o.isDeleted = ?2")
   Offer findByBarbershopIdAndDeletedIs(Long id, boolean b);
   Offer findByBarbershopId(Long id);

}