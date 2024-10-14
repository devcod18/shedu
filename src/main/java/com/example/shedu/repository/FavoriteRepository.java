package com.example.shedu.repository;


import com.example.shedu.entity.Favourite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favourite, Long> {

    Page<Favourite> findAll(PageRequest pageRequest);

}
