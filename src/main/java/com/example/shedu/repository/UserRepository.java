package com.example.shedu.repository;


import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    int countByUserRoleAndEnabledTrue(UserRole role);

    User findByIdAndUserRoleAndEnabledTrue(Long id, UserRole role);

    List<User> findByUserRole(UserRole role);

    Optional<User> findByActivationCode(Integer activationCode);

    @Query("SELECT u FROM users u WHERE " +
            "(:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) AND " +
            "(:phoneNumber IS NULL OR u.phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    List<User> searchByFields(@Param("fullName") String fullName,
                              @Param("phoneNumber") String phoneNumber,
                              @Param("email") String email);

    Page<User> findAllByRole(UserRole role, PageRequest pageRequest);

    Optional<User> findById(Long id);}
