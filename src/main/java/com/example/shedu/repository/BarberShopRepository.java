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

@Repository
public interface BarberShopRepository extends JpaRepository<Barbershop, Long> {



    @Query("select b from Barbershop b where b.owner.id = ?1 and b.isActive = true and b.title= ?2 ")
    List<Barbershop> findBarbershopByOwner(Long ownerId,String title);

    @Query("select b from Barbershop b where b.owner.id= ?1 and b.id=?2 and b.isActive= true ")
    Barbershop findByIdAndOwnerAndActiveTrue(Long id,Long barber_id);


    @Query("""
       select b from Barbershop b
       where LOWER(b.title) like LOWER(concat('%', ?1, '%'))
       and b.isActive = true and b.region=?2
       """)
    List<Barbershop> findByTitleContainingIgnoreCase(String title,BarbershopRegion region);
// search uchun
    @Query("select b from Barbershop b where b.isActive = true order by b.id desc ")
    Page<Barbershop> FindAllByActive(Pageable pageable);

    @Query("select b from Barbershop b where b.owner.id= ?1 and b.isActive=true ")
    Barbershop findByOwner(Long id);




}


