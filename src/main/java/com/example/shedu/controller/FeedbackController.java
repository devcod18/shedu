package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.RatingCategory;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqFeedback;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@CrossOrigin
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/addFeedback")
    @Operation(summary = "Fikr qo'shish", description = "Foydalanuvchilarga sartarosh yoki sartaroshxona uchun fikr qo'shish imkoniyatini beradi.")
    public ResponseEntity<ApiResponse> addFeedback(@RequestBody ReqFeedback reqFeedback, @CurrentUser User user) {
        ApiResponse apiResponse = feedbackService.addFeedback(reqFeedback, user);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/feedbacks/rating")
    @Operation(summary = "Reyting bo'yicha fikrlarni olish", description = "Barbershop uchun berilgan fikrlarni reyting kategoriyasi bo'yicha olish imkonini beradi.")
    public ApiResponse getFeedbacksByRatingCategory(
            @RequestParam Long barbershopId,
            @RequestParam RatingCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return feedbackService.getFeedbackByRatingCategory(barbershopId, category, page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_USER')")
    @DeleteMapping("/deleteFeedback/{deleteId}")
    @Operation(summary = "Fikrni o'chirish", description = "Foydalanuvchilarga o'z fikrlarini o'chirish imkoniyatini beradi.")
    public ResponseEntity<ApiResponse> deleteFeedback(@PathVariable Long deleteId) {
        ApiResponse apiResponse = feedbackService.deleteFeedback(deleteId);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{feedbackId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Fikrni tahrirlash")
    public ResponseEntity<ApiResponse> updateFeedback(
        @CurrentUser User user,
        @PathVariable Long feedbackId,
        @RequestBody ReqFeedback reqFeedback
    ){
        ApiResponse apiResponse = feedbackService.updateFeedback(feedbackId, reqFeedback, user);
        return ResponseEntity.ok(apiResponse);
    }
}