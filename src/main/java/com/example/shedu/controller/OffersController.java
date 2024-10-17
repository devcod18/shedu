package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqOffers;
import com.example.shedu.service.OffersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OffersController {

    private final OffersService offersService;

    @PostMapping("/addOffers")
    public ResponseEntity<ApiResponse> addOffers(ReqOffers reqOffers) {
        ApiResponse apiResponse = offersService.addService(reqOffers);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getAllOffers")
    public ResponseEntity<ApiResponse> getAllOffers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size ) {
        ApiResponse allServices = offersService.getAllOffers(page, size);
        return ResponseEntity.ok(allServices);
    }
}
