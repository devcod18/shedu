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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/getMe")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_MODERATOR','ROLE_MASTER','ROLE_BARBER')")
    public ResponseEntity<ApiResponse> showProfile(@CurrentUser User user) {
        ApiResponse me = userService.getMe(user);
        return ResponseEntity.ok(me);
    }

    @GetMapping("getAllUsers")
    @PreAuthorize("hasAnyRole()('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam int page, @RequestParam int size, @RequestParam UserRole role) {
        ApiResponse allUsersByRole = userService.getAllUsersByRole(page, size, role);
        return ResponseEntity.ok(allUsersByRole);
    }

    @PutMapping("/updateUser")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_MODERATOR','ROLE_MASTER','ROLE_BARBER')")
    public ResponseEntity<ApiResponse> updateUser(@CurrentUser User user, @RequestBody AuthRegister authRegister) {
        ApiResponse apiResponse = userService.updateUser(user, authRegister);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> searchUser(@RequestParam String field, @RequestParam UserRole role) {
        ApiResponse apiResponse = userService.searchUserByRole(field, role);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/changeStatus/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> enableDisableUser(@PathVariable Long userId, @RequestParam boolean enable) {
        ApiResponse apiResponse = userService.enableUser(userId, enable);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/deleteUser/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        ApiResponse apiResponse = userService.deleteUser(userId);
        return ResponseEntity.ok(apiResponse);
    }
}