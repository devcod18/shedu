package com.example.shedu.repository;

import com.example.shedu.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserId(Long userId);
    List<Orders> findByBarbershopId(Long id);
}


