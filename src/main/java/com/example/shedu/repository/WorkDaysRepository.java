package com.example.shedu.repository;


import com.example.shedu.entity.WorkDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface WorkDaysRepository extends JpaRepository<WorkDays, Long> {


   boolean existsByBarbershopId_Id(Long id);


   Optional<WorkDays> findByBarbershopId_Id(Long barbershopId);



}
