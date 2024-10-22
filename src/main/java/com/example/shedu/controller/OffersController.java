package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqOffers;
import com.example.shedu.service.OffersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OffersController {

    private final OffersService offersService;

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PostMapping("/addOffers")
    public ResponseEntity<ApiResponse> addOffers(@RequestBody ReqOffers reqOffers) {
        ApiResponse apiResponse = offersService.addService(reqOffers);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getAllOffers")
    public ResponseEntity<ApiResponse> getAllOffers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam boolean isDeleted) {
        ApiResponse allOffers = offersService.getAllOffers(page, size, isDeleted);
        return ResponseEntity.ok(allOffers);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PutMapping("/updateOffers/{OffersId}")
    public ResponseEntity<ApiResponse> updateOffer(@PathVariable Long OffersId, @RequestBody ReqOffers reqOffers) {
        ApiResponse apiResponse = offersService.updateOffer(OffersId, reqOffers);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("/deleteOffers/{OffersId}")
    public ResponseEntity<ApiResponse> deleteOffer(@PathVariable Long OffersId) {
        ApiResponse apiResponse = offersService.deleteOffer(OffersId);
        return ResponseEntity.ok(apiResponse);
    }
}


