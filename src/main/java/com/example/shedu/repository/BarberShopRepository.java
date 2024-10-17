package com.example.shedu.repository;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.enums.BarbershopRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarberShopRepository extends JpaRepository<Barbershop, Long> {

    @Query("SELECT b FROM Barbershop b WHERE b.region = ?1 AND b.isActive = true")
    List<Barbershop> findByRegionAndIsActive(BarbershopRegion region);

    @Query("SELECT b FROM Barbershop b JOIN b.owner u WHERE u.userRole = ?1 AND b.isActive = true")
    List<Barbershop> findAllByMaster(String userRole);

    Optional<Barbershop> findById(Long id);

    Barbershop findByTitle(String title);

    boolean existsById(Long id);

    List<Barbershop> findAllByTitle(String title);

    @Query("SELECT b FROM Barbershop b WHERE UPPER(b.title) LIKE UPPER(CONCAT('%', ?1, '%')) AND b.isActive = true")
    List<Barbershop> findByTitleAndActive(String title);
}
