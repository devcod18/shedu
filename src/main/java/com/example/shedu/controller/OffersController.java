
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
@RequestMapping("/offers")
@RequiredArgsConstructor
@CrossOrigin
public class OffersController {

    private final OffersService offersService;

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PostMapping("/addOffers/{barbershopId}")
    @Operation(summary = "Barbershopga qo'shish ", description = "Barbershopga qo'shish")
    public ResponseEntity<ApiResponse> addOffers(@PathVariable Long barbershopId,@RequestBody ReqOffers reqOffers) {
        ApiResponse apiResponse = offersService.create(reqOffers,barbershopId);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getByBarbershop/{barbershopId}/")
    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @Operation(summary = "Offerlarni ko'rish", description = "Barbershopni Offerlarni ko'rish")
    public ResponseEntity<ApiResponse> getByBarbershop(@PathVariable Long barbershopId,@RequestParam Boolean b) {
        ApiResponse apiResponse = offersService.getOffersByBarberShop(barbershopId,b);
        return ResponseEntity.ok(apiResponse);
    }
    @Operation(summary = "Barcha Offerlarni ko'rish", description = "Barcha Offerlarni ko'rish")
    @GetMapping("/getAllOffers")
    public ResponseEntity<ApiResponse> getAllOffers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        ApiResponse allOffers = offersService.getAllOffers(page, size);
        return ResponseEntity.ok(allOffers);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PutMapping("/updateOffers/{Id}/{barbershopId}")
    @Operation(summary = "Offerlarni o'zgartirish", description = "Offerlarni o'zgartirish")
    public ResponseEntity<ApiResponse> updateOffer(@PathVariable Long Id, @RequestBody ReqOffers reqOffers,
                                                   @PathVariable Long barbershopId) {
        ApiResponse apiResponse = offersService.update(reqOffers, Id, barbershopId);
        return ResponseEntity.ok(apiResponse);
    }
    @Operation(summary = "Offerlarni o'chirish", description = "Offerlarni o'chirish")
    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("/deleteOffers/{deleteOffersId}")
    public ResponseEntity<ApiResponse> deleteOffer(@PathVariable Long deleteOffersId) {
        ApiResponse apiResponse = offersService.isActive(deleteOffersId);
        return ResponseEntity.ok(apiResponse);
    }

//    @GetMapping("/search")
//    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
//    @Operation(summary = "Offerlarni qidirish ", description = "Offerlarni qidirish")
//    public ResponseEntity<ApiResponse> search(
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "5") int size,
//            @RequestParam(name = "name") String name,
//            @RequestParam ServiceType serviceType){
//        ApiResponse apiResponse=offersService.search(name,serviceType,page,size);
//        return ResponseEntity.ok(apiResponse);
//    }


}