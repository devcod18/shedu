package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarbershopRegion;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqBarbershop;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.BarbershopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/barbershop")
public class BarbershopController {

    private final BarbershopService barbershopService;

    // Barbershop qo'shish
    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveBarbershop(@RequestBody ReqBarbershop reqBarbershop, @CurrentUser User user, @RequestParam BarbershopRegion region) {
        ApiResponse apiResponse = barbershopService.save(reqBarbershop, user, region);
        return ResponseEntity.ok(apiResponse);
    }

    // Barcha barbershoplarni olish
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllBarbershops(@RequestParam int page, @RequestParam int size) {
        ApiResponse apiResponse = barbershopService.getAll(size, page);
        return ResponseEntity.ok(apiResponse);
    }

    // Barbershopni o'chirish
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteBarbershop(@PathVariable Long id) {
        ApiResponse apiResponse = barbershopService.delete(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Barbershopni yangilash
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateBarbershop(@RequestParam Long userId,
                                                        @RequestBody ReqBarbershop reqBarbershop,
                                                        @RequestParam Long barbershopId,
                                                        @RequestParam BarbershopRegion region) {
        ApiResponse apiResponse = barbershopService.update(userId, reqBarbershop, barbershopId, region);
        return ResponseEntity.ok(apiResponse);
    }

    // Barbershopni nomi va region bo'yicha qidirish
    @PreAuthorize("hasAnyRole('ROLE_SUPEER_ADMIN','ROLE_ADMIN','ROLE_USER','ROLE_MASTER')")

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchBarbershops(@RequestParam String title, @RequestParam BarbershopRegion region) {
        ApiResponse apiResponse = barbershopService.search(title, region);
        return ResponseEntity.ok(apiResponse);
    }
}
