package com.example.shedu.repository;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByPhoneNumber(String phoneNumber);

//    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
//
//    int countByUserRoleAndEnabledTrue(UserRole role);

    Optional<User> findByIdAndUserRoleAndEnabledTrue(Long id, UserRole role);

//    List<User> findByUserRole(UserRole role);
//
//    Optional<User> findByActivationCode(Integer activationCode);

//    @Query(value = "SELECT * FROM users u WHERE " +
//            "(:fullName IS NULL OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :fullName, '%'))) AND " +
//            "(:phoneNumber IS NULL OR u.phone_number LIKE CONCAT('%', :phoneNumber, '%')) AND " +
//            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))", nativeQuery = true)
//    List<User> findUsersByFullNameOrPhoneNumberOrEmail(@Param("fullName") String fullName,
//                              @Param("phoneNumber") String phoneNumber,
//                              @Param("email") String email);



    Page<User> findAllByUserRole(UserRole role, Pageable pageable);

    Optional<User> findByEmail(String email);
}
