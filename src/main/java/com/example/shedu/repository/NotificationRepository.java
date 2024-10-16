package com.example.shedu.repository;


import com.example.shedu.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Integer countAllByUserIdAndReadFalse(Long userId);

    List<Notification> findAllByUserId(Long userId);

    Notification findByRegistrantId(Long userId);
}
