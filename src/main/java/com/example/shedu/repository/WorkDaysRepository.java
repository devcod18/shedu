package com.example.shedu.repository;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.WorkDays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkDaysRepository extends JpaRepository<WorkDays, Long> {
   List<WorkDays> findAllByBarbershopId(Barbershop barbershop);

}
