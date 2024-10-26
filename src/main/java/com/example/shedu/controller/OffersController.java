package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqOffers;
import com.example.shedu.service.OffersService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
@CrossOrigin
public class OffersController {

    private final OffersService offersService;

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PostMapping("/addOffers")
    @Operation(summary = "Taklif qo'shish", description = "Yangi xizmat taklifini qo'shish imkonini beradi.")
    public ResponseEntity<ApiResponse> addOffers(@RequestBody ReqOffers reqOffers) {
        ApiResponse apiResponse = offersService.addService(reqOffers);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getAllOffers")
    @Operation(summary = "Barcha takliflarni olish", description = "Barcha mavjud xizmat takliflarini qaytaradi.")
    public ResponseEntity<ApiResponse> getAllOffers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        ApiResponse allOffers = offersService.getAllOffers(page, size);
        return ResponseEntity.ok(allOffers);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PutMapping("/updateOffers/{updateOffersId}")
    @Operation(summary = "Taklifni yangilash", description = "Mavjud xizmat taklifini yangilash imkonini beradi.")
    public ResponseEntity<ApiResponse> updateOffer(@PathVariable Long updateOffersId, @RequestBody ReqOffers reqOffers) {
        ApiResponse apiResponse = offersService.updateOffer(updateOffersId, reqOffers);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("/deleteOffers/{deleteOffersId}")
    @Operation(summary = "Taklifni o'chirish", description = "Muayyan xizmat taklifini o'chirish imkonini beradi.")
    public ResponseEntity<ApiResponse> deleteOffer(@PathVariable Long deleteOffersId) {
        ApiResponse apiResponse = offersService.deleteOffer(deleteOffersId);
        return ResponseEntity.ok(apiResponse);
    }
}