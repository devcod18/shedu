package com.example.shedu.repository;

import com.example.shedu.entity.OfferType;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface OfferTypeRepository extends JpaRepository<OfferType, Long> {
    Boolean  existsByTitle(String title);
    List<OfferType> findAllByTitle(String s);
    OfferType findByTitle(String s);
}