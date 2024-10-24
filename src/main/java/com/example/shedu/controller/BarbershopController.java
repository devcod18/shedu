package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarbershopRegion;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqBarbershop;
import com.example.shedu.payload.req.ReqWorkDays;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.BarbershopService;
import com.example.shedu.service.WorkDaysService;
import io.swagger.v3.oas.annotations.Operation; // Swagger annotatsiyasi uchun import
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/barbershop")
@CrossOrigin
public class BarbershopController {

    private final BarbershopService barbershopService;
    private final WorkDaysService workDaysService;


    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN')")
    @PostMapping("/save")
    @Operation(summary = "Yangi barbershopni saqlash", description = "Master yoki Super Admin yangi barbershop qo'shishi mumkin.")
    public ResponseEntity<ApiResponse> saveBarbershop(@RequestBody ReqBarbershop reqBarbershop,
                                                      @CurrentUser User user,
                                                      @RequestParam BarbershopRegion region) {
        ApiResponse apiResponse = barbershopService.save(reqBarbershop, user, region);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_MASTER','ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/all")
    @Operation(summary = "Barcha barbershoplarni olish", description = "Foydalanuvchilar barcha barbershoplarni ko'rishi mumkin.")
    public ResponseEntity<ApiResponse> getAllBarbershops(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        ApiResponse apiResponse = barbershopService.getAll(size, page);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_MASTER')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Barbershopni o'chirish", description = "Super Admin yoki Master barbershopni o'chirishi mumkin.")
    public ResponseEntity<ApiResponse> deleteBarbershop(@PathVariable Long id) {
        ApiResponse apiResponse = barbershopService.delete(id);
        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_MASTER')")
    @PutMapping("/update")
    @Operation(summary = "Barbershopni yangilash", description = "Barbershop ma'lumotlarini yangilash mumkin.")
    public ResponseEntity<ApiResponse> updateBarbershop(@CurrentUser User user,
                                                        @RequestParam Long barbershopId,
                                                        @RequestParam BarbershopRegion region,
                                                        @RequestBody ReqBarbershop reqBarbershop) {
        ApiResponse apiResponse = barbershopService.update(user, reqBarbershop, barbershopId, region);
        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER','ROLE_MASTER')")
    @GetMapping("/search")
    @Operation(summary = "Barbershop qidirish", description = "Barbershoplarni sarlavha va region bo'yicha qidirish.")
    public ResponseEntity<ApiResponse> searchBarbershops(@RequestParam String title,
                                                         @RequestParam BarbershopRegion region) {
        ApiResponse apiResponse = barbershopService.search(title, region);
        return ResponseEntity.ok(apiResponse);
    }


    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @GetMapping("/getByOwner")
    @Operation(summary = "Egasiga tegishli barbershoplarni olish", description = "Master o'ziga tegishli barbershoplarni olishi mumkin.")
    public ResponseEntity<ApiResponse> getBarbershopByOwner(@CurrentUser User user) {
        ApiResponse apiResponse = barbershopService.getByOwner(user);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PostMapping("/saveWorkdays")
    @Operation(summary = "Ish kunlarini saqlash", description = "Master o'z ish kunlarini saqlashi mumkin.")
    public ResponseEntity<ApiResponse> saveWorkDays(@Valid @RequestBody ReqWorkDays days) {
        ApiResponse apiResponse = workDaysService.saveWorkDays(days);
        return ResponseEntity.ok(apiResponse);
    }

     @PreAuthorize("hasAnyRole('ROLE_MASTER')")
    @PutMapping("/updateWorks")
    @Operation(summary = "Ish kunlarini yangilash", description = "Master o'z ish kunlarini yangilashi mumkin.")
    public ResponseEntity<ApiResponse> updateWorkDays(@RequestBody ReqWorkDays days) {
        ApiResponse apiResponse = workDaysService.update(days);
        return ResponseEntity.ok(apiResponse);
    }
}
