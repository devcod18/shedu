package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqFavorite;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
@CrossOrigin
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/addFavorite")
    @Operation(summary = "Favorit qo'shish", description = "Foydalanuvchilarga favorit element qo'shish imkonini beradi.")
    public ResponseEntity<ApiResponse> addFavorite(
            @RequestBody ReqFavorite reqFavorite,
            @CurrentUser User user) {

        ApiResponse response = favoriteService.addFavorite(reqFavorite, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllFavorites")
    @Operation(summary = "Barcha favoritlarni olish", description = "Foydalanuvchining barcha favorit elementlarini qaytaradi.")
    public ResponseEntity<ApiResponse> getAllFavorites(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @CurrentUser User user) {

        ApiResponse allFavorites = favoriteService.getAllFavorites(page, size, user);
        return ResponseEntity.ok(allFavorites);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/{deleteId}")
    @Operation(summary = "Favoritni o'chirish", description = "Muayyan favorit elementni o'chirish imkonini beradi.")
    public ResponseEntity<ApiResponse> deleteFavorite(@PathVariable Long deleteId) {
        ApiResponse response = favoriteService.deleteFavorite(deleteId);
        return ResponseEntity.ok(response);
    }

}