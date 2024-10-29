package com.example.shedu.repository;

import com.example.shedu.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    @Query("SELECT c FROM Chat c WHERE c.sender.id = :userId OR c.receiver.id = :userId")
    Page<Chat> findAllBySenderIdOrReceiverId(Long userId, Pageable pageable);

    boolean existsByReceiverId(Long receiverId);
}
