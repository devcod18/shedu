package com.example.shedu.repository;


import com.example.shedu.entity.Barbershop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarberShopRepository extends JpaRepository<Barbershop, Long> {

    Optional<Barbershop> findById(Long id);

}
