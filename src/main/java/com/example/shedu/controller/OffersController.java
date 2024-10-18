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
    public ResponseEntity<ApiResponse> addOffers(@RequestBody ReqOffers reqOffers) {
        ApiResponse apiResponse = offersService.addService(reqOffers);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getAllOffers")
    public ResponseEntity<ApiResponse> getAllOffers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        ApiResponse allOffers = offersService.getAllOffers(page, size);
        return ResponseEntity.ok(allOffers);
    }

    @PutMapping("/updateOffers/{OffersId}")
    public ResponseEntity<ApiResponse> updateOffer(@PathVariable Long OffersId, @RequestBody ReqOffers reqOffers) {
        ApiResponse apiResponse = offersService.updateOffer(OffersId, reqOffers);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/deleteOffers/{OffersId}")
    public ResponseEntity<ApiResponse> deleteOffer(@PathVariable Long OffersId) {
        ApiResponse apiResponse = offersService.deleteOffer(OffersId);
        return ResponseEntity.ok(apiResponse);
    }
}
