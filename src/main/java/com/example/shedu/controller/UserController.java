package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.auth.AuthRegister;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Validated
@CrossOrigin
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_MODERATOR','ROLE_MASTER','ROLE_BARBER')")
    @Operation(summary = "Joriy foydalanuvchi profilini olish", description = "Tizimga kirgan foydalanuvchi o'z profilini ko'rishi mumkin.")
    public ResponseEntity<ApiResponse> getCurrentUserProfile(@CurrentUser User user) {
        ApiResponse response = userService.getMe(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN')")
    @Operation(summary = "Barcha foydalanuvchilar ro'yxatini ko'rish", description = "Admin va super admin huquqiga ega foydalanuvchilar barcha foydalanuvchilar ro'yxatini ko'rishi mumkin.")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam UserRole role) {
        ApiResponse response = userService.getAllUsersByRole(page, size, role);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_MODERATOR','ROLE_MASTER','ROLE_BARBER')")
    @Operation(summary = "Foydalanuvchi profilini yangilash", description = "Tizimga kirgan foydalanuvchi o'z profilini yangilashi mumkin.")
    public ResponseEntity<ApiResponse> updateUser(@CurrentUser User user, @RequestBody AuthRegister authRegister) {
        ApiResponse response = userService.updateUser(user, authRegister);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @Operation(summary = "Foydalanuvchilarni rol bo'yicha qidirish", description = "Admin va super admin huquqiga ega foydalanuvchilar ma'lum bir rol bo'yicha foydalanuvchilarni qidirishi mumkin.")
    public ResponseEntity<ApiResponse> searchUserByRole(@RequestParam String field, @RequestParam UserRole role) {
        ApiResponse response = userService.searchUserByRole(field, role);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/changeStatus/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @Operation(summary = "Foydalanuvchi statusini o'zgartirish", description = "Admin va super admin huquqiga ega foydalanuvchilar foydalanuvchi statusini yoqish yoki o'chirishlari mumkin.")
    public ResponseEntity<ApiResponse> changeUserStatus(@PathVariable Long userId, @RequestParam boolean enable) {
        ApiResponse response = userService.enableUser(userId, enable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    @Operation(summary = "Foydalanuvchini o'chirish", description = "Admin va super admin huquqiga ega foydalanuvchilar foydalanuvchini tizimdan o'chirishlari mumkin.")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        ApiResponse response = userService.deleteUser(userId);
        return ResponseEntity.ok(response);
    }
}


