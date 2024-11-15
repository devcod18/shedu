package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqOfferType;
import com.example.shedu.service.OfferTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offerType")
@RequiredArgsConstructor
@CrossOrigin
public class OfferTypeController {
    private final OfferTypeService offerTypeService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/save")
    @Operation(summary = "OfferType qushadi ", description = "OfferType saqlash")
    public ResponseEntity<ApiResponse> save(
            @Valid @RequestBody ReqOfferType offerType
    ) {
        ApiResponse apiResponse = offerTypeService.save(offerType);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/update/{Id}")
    @Operation(summary = "OfferType UPDATE ", description = "OfferType UPDATE")
    public ResponseEntity<ApiResponse> update(
            @Valid @RequestBody ReqOfferType reqOfferType,
            @PathVariable Long Id
    ) {
        ApiResponse apiResponse = offerTypeService.update(reqOfferType, Id);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MASTER')")
    @GetMapping("/getAll")
    @Operation(summary = "OfferType ALL ", description = "OfferType ALL")
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam String s
    ) {
        ApiResponse apiResponse = offerTypeService.getAll(s);
        return ResponseEntity.ok(apiResponse);
    }

}
