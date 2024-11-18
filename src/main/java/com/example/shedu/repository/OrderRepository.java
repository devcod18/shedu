package com.example.shedu.repository;

import com.example.shedu.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserId(Long userId);
    List<Orders> findByBarbershopId(Long id);
    @Query("""
    SELECT COUNT(o) > 0
    FROM Orders o
    WHERE o.barbershop.id = :barbershopId
      AND o.bookingDay = :bookingDay
      AND (
           (o.startBooking <= :endBooking AND o.endBooking >= :startBooking)
      )
""")
    boolean existsByBarbershopIdAndBookingDayAndStartBookingLessThanEqualAndEndBookingGreaterThanEqual(
            @Param("barbershopId") Long barbershopId,
            @Param("bookingDay") LocalDate bookingDay,
            @Param("startBooking") LocalTime startBooking,
            @Param("endBooking") LocalTime endBooking);

}


