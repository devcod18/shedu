package com.example.shedu.repository;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    User findByIdAndUserRoleAndEnabledTrue(Long id, UserRole role);

    @Query("SELECT u FROM users u WHERE " +
            "(:role IS NULL OR u.userRole = :role) AND " +
            "(" +
            "(:field IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :field, '%'))) OR " +
            "(:field IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :field, '%'))) OR " +
            "(:field IS NULL OR u.phoneNumber LIKE CONCAT('%', :field, '%'))" +
            ")")
    List<User> searchByFieldsAndUserRole(@Param("field") String field,
                                         @Param("role") UserRole role);

    Page<User> findAllByUserRole(UserRole role, Pageable pageable);
}