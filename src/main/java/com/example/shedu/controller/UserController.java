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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_MODERATOR','ROLE_MASTER','ROLE_BARBER')")
    public ResponseEntity<ApiResponse> getCurrentUserProfile(@CurrentUser User user) {
        ApiResponse response = userService.getMe(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam int page, @RequestParam int size, @RequestParam UserRole role) {
        ApiResponse response = userService.getAllUsersByRole(page, size, role);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_MODERATOR','ROLE_MASTER','ROLE_BARBER')")
    public ResponseEntity<ApiResponse> updateUser(@CurrentUser User user,@RequestBody AuthRegister authRegister) {
        ApiResponse response = userService.updateUser(user, authRegister);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> searchUserByRole(@RequestParam String field, @RequestParam UserRole role) {
        ApiResponse response = userService.searchUserByRole(field, role);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/status/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> changeUserStatus(@PathVariable Long userId, @RequestParam boolean enable) {
        ApiResponse response = userService.enableUser(userId, enable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        ApiResponse response = userService.deleteUser(userId);
        return ResponseEntity.ok(response);
    }
}
