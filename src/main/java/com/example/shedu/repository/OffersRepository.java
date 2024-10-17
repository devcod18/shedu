package com.example.shedu.repository;

import com.example.shedu.entity.Offers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffersRepository extends JpaRepository<Offers, Long> {

    Page<Offers> finAll(Pageable pageable);

}
