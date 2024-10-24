package com.example.shedu.controller;

import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.auth.AuthLogin;
import com.example.shedu.payload.auth.AuthRegister;
import com.example.shedu.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login", description = "Foydalanuvchini tizimga kiritish")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> logIn(@Valid @RequestBody AuthLogin authLogin) {
        return ResponseEntity.ok(authService.login(authLogin));
    }

    @Operation(summary = "Register", description = "Foydalanuvchini ro'yxatdan o'tkazish")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRegister authRegister,
                                                @RequestParam UserRole userRole) {
        return ResponseEntity.ok(authService.register(authRegister, userRole));
    }

    @Operation(summary = "Admin yoki Master tomonidan yangi admin qo'shish", description = "Admin yoki Master yangi administratorni tizimga qo'shishi mumkin")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_MASTER')")
    @PostMapping("/admin/save-admin")
    public ResponseEntity<ApiResponse> adminSaveTeacher(@Valid @RequestBody AuthRegister auth) {
        return ResponseEntity.ok(authService.adminSaveLibrarian(auth));
    }

    @Operation(summary = "Kod tekshirish", description = "Ro'yxatdan o'tgandan so'ng kodni tekshirish")
    @PutMapping("/check-code")
    public ResponseEntity<ApiResponse> checkCode(@RequestParam Integer code) {
        return ResponseEntity.ok(authService.checkCode(code));
    }
}
