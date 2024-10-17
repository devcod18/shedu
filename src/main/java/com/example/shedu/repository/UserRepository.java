package com.example.shedu.repository;

import com.example.shedu.entity.Feedback;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    int countByUserRoleAndEnabledTrue(UserRole role);

    User findByIdAndUserRoleAndEnabledTrue(Long id, UserRole role);

    List<User> findByUserRole(UserRole role);

    Optional<User> findByActivationCode(Integer activationCode);

    @Query( value = "SELECT u FROM users u WHERE\n" +
            "            (:fullName IS NULL OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :fullName, '%'))) AND\n" +
            "            (:phoneNumber IS NULL OR u.phone_number LIKE CONCAT('%', :phoneNumber, '%')) AND\n" +
            "            (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))", nativeQuery = true)
    List<User> searchByFields(@Param("fullName") String fullName,
                              @Param("phoneNumber") String phoneNumber,
                              @Param("email") String email);

    Page<User> findAllByUserRole(UserRole role, PageRequest pageRequest);

//    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);



}
