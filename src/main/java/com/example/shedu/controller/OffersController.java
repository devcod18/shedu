
package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqOffer;
import com.example.shedu.service.OffersService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
@CrossOrigin
public class OffersController {

    private final OffersService offersService;

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PostMapping("/addOffer")
    @Operation(summary = "Barbershopga qo'shish ", description = "Barbershopga qo'shish")
    public ResponseEntity<ApiResponse> create(@RequestBody ReqOffer reqOffers,
    @RequestParam Long Id) {
        ApiResponse apiResponse = offersService.create(reqOffers,Id);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getByBarbershop")
    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @Operation(summary = "Offerlarni ko'rish", description = "Barbershopni Offerlarni ko'rish")
    public ResponseEntity<ApiResponse> getByBarbershop(
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        ApiResponse apiResponse = offersService.getAll( page, size);
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Barcha Offerlarni ko'rish", description = "Barcha Offerlarni ko'rish")
    @GetMapping("/getAllOffers")
    public ResponseEntity<ApiResponse> getAllOffers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        ApiResponse allOffers = offersService.getAll(page, size);
        return ResponseEntity.ok(allOffers);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PutMapping("/updateOffers/{Id}")
    @Operation(summary = "Offerlarni o'zgartirish", description = "Offerlarni o'zgartirish")
    public ResponseEntity<ApiResponse> updateOffer(@RequestBody ReqOffer reqOffers,
                                                   @PathVariable Long Id) {
        ApiResponse apiResponse = offersService.update(reqOffers, Id);
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Offerlarni o'chirish", description = "Offerlarni o'chirish")
    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("/deleteOffers/{deleteOffersId}")
    public ResponseEntity<ApiResponse> deleteOffer(@PathVariable Long deleteOffersId) {
        ApiResponse apiResponse = offersService.delete(deleteOffersId);
        return ResponseEntity.ok(apiResponse);
    }

}