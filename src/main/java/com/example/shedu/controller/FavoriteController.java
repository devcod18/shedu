package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqFavorite;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addFavorite(
            @RequestBody ReqFavorite reqFavorite,
            @CurrentUser User user) {

        ApiResponse response = favoriteService.addFavorite(reqFavorite, user);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getAllFavorites")
    public ResponseEntity<ApiResponse> getAllFavorites(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        ApiResponse allFavorites = favoriteService.getAllFavorites(page, size);
        return ResponseEntity.ok(allFavorites);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/{deletedId}")
    public ResponseEntity<ApiResponse> deleteFavorite(@PathVariable Long deletedId) {
        ApiResponse response = favoriteService.deleteFavorite(deletedId);
        return ResponseEntity.ok(response);
    }
}