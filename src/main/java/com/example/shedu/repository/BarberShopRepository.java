package com.example.shedu.repository;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarbershopRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("SELECT b FROM Barbershop b Where b.title = ?1 AND b.isActive = true and b.owner= ?2")
    Barbershop findAllByTitleAndActiveTrueAndOwner(String title, User owner);

    boolean existsById(Long id);

    List<Barbershop> findAllByTitle(String title);

    @Query("SELECT b FROM Barbershop b JOIN b.region r WHERE b.title LIKE CONCAT('%', UPPER(?1), '%', LOWER(?1), '%') AND b.isActive = true")
    List<Barbershop> findByTitleAndRegionAndActive(String title, String barbershopRegion);

    @Query("select b from Barbershop b where b.isActive = true")
    Page<Barbershop> FindAllByActive(Pageable pageable);

    @Query("select b from Barbershop b where b.id = ?1 and b.isActive = true")
    Barbershop FindByIdAndActive(Long id);

}


