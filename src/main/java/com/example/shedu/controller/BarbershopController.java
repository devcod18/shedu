package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarbershopRegion;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqBarbershop;
import com.example.shedu.payload.req.ReqWorkDays;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.BarbershopService;
import com.example.shedu.service.WorkDaysService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/barbershop")
@CrossOrigin
public class BarbershopController {

    private final BarbershopService barbershopService;
    private final WorkDaysService workDaysService;


    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN')")
    @PostMapping("/save")
    @Operation(summary = "Barbershop yaratish", description = " Barbershop yaratish")
    public ResponseEntity<ApiResponse> saveBarbershop(@RequestBody ReqBarbershop reqBarbershop,
                                                      @CurrentUser User user,
                                                      @RequestParam BarbershopRegion region
    ) {
        ApiResponse apiResponse = barbershopService.save(reqBarbershop, user, region);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MASTER','ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/all")
    @Operation(summary = "Barbershoplarni ko'rish", description = "Barbershoplarni ko'rish")
    public ResponseEntity<ApiResponse> getAllBarbershops(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        ApiResponse apiResponse = barbershopService.getAll(size, page);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_MASTER')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Barbershopni o'chirish", description = "Barbershopni o'chirish")
    public ResponseEntity<ApiResponse> deleteBarbershop(@PathVariable Long id) {
        ApiResponse apiResponse = barbershopService.delete(id);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_MASTER')")
    @PutMapping("/update")
    @Operation(summary = "Barbershopni yangilash", description = "Barbershopni yangilash")
    public ResponseEntity<ApiResponse> updateBarbershop(@CurrentUser User user,
                                                        @RequestParam Long barbershopId,
                                                        @RequestParam BarbershopRegion region,
                                                        @RequestBody ReqBarbershop reqBarbershop
    ) {
        ApiResponse apiResponse = barbershopService.update(user, reqBarbershop, barbershopId, region);
        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPEER_ADMIN','ROLE_ADMIN','ROLE_USER','ROLE_MASTER')")
    @GetMapping("/search")
    @Operation(summary = "Barbershoplarni qidirish", description = "Barbershoplarni qidirish")
    public ResponseEntity<ApiResponse> searchBarbershops(@RequestParam String title,
                                                         @RequestParam BarbershopRegion region,
                                                         @RequestParam (defaultValue = "0" )int page,
                                                         @RequestParam (defaultValue = "10" )int size) {
        ApiResponse apiResponse = barbershopService.search(title, region, size, page);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @GetMapping("/getByOwner")
    @Operation(summary = "Barbershopni ko'rish", description = "Barbershopni ko'rish")
    public ResponseEntity<ApiResponse> getBarbershopByOwner(@CurrentUser User user) {
        ApiResponse apiResponse = barbershopService.getByOwner(user);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PostMapping("/saveWorkdays/{id}")
    @Operation(summary = "Ish kunlari yaratish", description = "Ish kunlari yaratish")
    public ResponseEntity<ApiResponse> save(@Valid @RequestBody ReqWorkDays days,
                                             @PathVariable Long id) {
        ApiResponse apiResponse = workDaysService.saveWorkDays(days,id);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PutMapping("/updateWorks/{id}")
    @Operation(summary = "Ish kunlari yangilash", description = "Ish kunlari yangilash")
    public ResponseEntity<ApiResponse> update(@RequestBody ReqWorkDays days
                                              ,@PathVariable Long id
    ) {
        ApiResponse apiResponse = workDaysService.update(days,id);
        return ResponseEntity.ok(apiResponse);
    }
}