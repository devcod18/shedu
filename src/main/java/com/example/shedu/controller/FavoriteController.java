package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqFavorite;
import com.example.shedu.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/addFavorite")
    public ResponseEntity<ApiResponse> addFavorite(ReqFavorite reqFavorite) {
        return ResponseEntity.ok(favoriteService.addFavorite(reqFavorite));
    }

    @GetMapping("/getAllFavorites")
    public ResponseEntity<ApiResponse> getAllFavorites(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(favoriteService.getAllFavorites(page, size));
    }

//
//    @GetMapping("/getOneFavorite/{getOneId}")
//    public ResponseEntity<ApiResponse> getOneFavorite(@PathVariable Long getOneId) {
//        return ResponseEntity.ok(favoriteService.getOneFavorite(getOneId));
//    }

    @DeleteMapping("/deleteFavorite/{deleteId}")
    public ResponseEntity<ApiResponse> deleteFavorite(@PathVariable Long deleteId) {
        return ResponseEntity.ok(favoriteService.deleteFavorite(deleteId));
    }


}
