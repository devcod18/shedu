package com.example.shedu.repository;


import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarbershopRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BarberShopRepository extends JpaRepository<Barbershop, Long> {
    @Query("select b from Barbershop b where b.region = ?1 and b.isActive = true")
    List<Barbershop> findByRegionAndIsActive(BarbershopRegion region);
//    @Query("select b from Barbershop  b  join  users as u on b.owner=u.id where u.user_role=?1 AND b.is_active=true")
//    List<Barbershop> findAllByMaster();


  Optional<Barbershop> findById(Long id);

    //    Optional<Barbershop> findByTitle(String title);
    Barbershop findByTitle(String title);
    boolean existsById(Long Id);

    List<Barbershop> findAllByTitle(String title);

    @Query("select b from Barbershop b where upper(b.title) like upper(concat('%', ?1, '%')) and b.isActive = true")
    List<Barbershop> findByTitleAndActive(String title);




}