package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqOffer;
import com.example.shedu.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
@CrossOrigin
public class OfferController {

    private final OfferService offersService;

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PostMapping("/addOffer")
    @Operation(summary = "Barbershopga qo'shish ", description = "Barbershopga qo'shish")
    public ResponseEntity<ApiResponse> create(@RequestBody ReqOffer reqOffers) {
        ApiResponse apiResponse = offersService.create(reqOffers);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getByBarbershop/{barbershopId}/")
    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @Operation(summary = "Offerlarni ko'rish", description = "Barbershopni Offerlarni ko'rish")
    public ResponseEntity<ApiResponse> getByBarbershop(@PathVariable Long barbershopId,
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "5") int size) {
        ApiResponse apiResponse = offersService.getAll(size, page);
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Barcha Offerlarni ko'rish", description = "Barcha Offerlarni ko'rish")
    @GetMapping("/getAllOffers")
    public ResponseEntity<ApiResponse> getAllOffers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        ApiResponse allOffers = offersService.getAll(size, page);
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

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PutMapping("/deleteOffers/{Id}")
    @Operation(summary = "Offerni deleted qilish ",description = "Offer deleted ")
    public ResponseEntity<ApiResponse> deleteOffer(@PathVariable Long Id,@RequestParam boolean b) {
        ApiResponse apiResponse=offersService.changeStatus(Id,b);
        return ResponseEntity.ok(apiResponse);
    }
}
