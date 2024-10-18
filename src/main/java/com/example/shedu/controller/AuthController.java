package com.example.shedu.controller;

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
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> logIn(@Valid @RequestBody AuthLogin authLogin) {
        return ResponseEntity.ok(authService.login(authLogin));
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRegister authRegister) {
        return ResponseEntity.ok(authService.register(authRegister));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/save-admin")
    public ResponseEntity<ApiResponse> adminSaveTeacher(@Valid @RequestBody AuthRegister auth) {
        return ResponseEntity.ok(authService.adminSaveLibrarian(auth));
    }
}
