package com.example.shedu.repository;

import com.example.shedu.entity.OfferType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OfferTypeRepository extends JpaRepository<OfferType, Long> {
    Boolean  existsByTitle(String title);

    @Query("select o from OfferType o where LOWER(o.title) like LOWER(concat('%', ?1, '%'))  order by o.id desc ")
    List<OfferType> findAllByTitle(String s);
    OfferType findByTitle(String s);

}
