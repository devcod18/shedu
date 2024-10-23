package com.example.shedu.repository;

import com.example.shedu.entity.Barbershop;
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
    Optional<Barbershop> findByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    Barbershop findByIdAndOwnerId(Long id, Long ownerId);

    @Query("""
            select b from Barbershop b
            where LOWER(b.title) like LOWER(concat('%', ?1, '%'))
            and b.isActive = true and b.region=?2
            """)
    List<Barbershop> findByTitleContainingIgnoreCase(String title, BarbershopRegion region);

    @Query("select b from Barbershop b where b.isActive = true order by b.id desc ")
    Page<Barbershop> FindAllByActive(Pageable pageable);

    @Query("select b from Barbershop b where b.owner.id= ?1 and b.isActive=true ")
    List<Barbershop> findByOwner(Long id);

}