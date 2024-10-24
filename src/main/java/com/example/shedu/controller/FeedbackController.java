package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.RatingCategory;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqFeedback;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@CrossOrigin
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/addFeedback")
    public ResponseEntity<ApiResponse> addFeedback(@RequestBody ReqFeedback reqFeedback, @CurrentUser User user) {
        ApiResponse apiResponse = feedbackService.addFeedback(reqFeedback, user);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/feedbacks/rating")
    public ApiResponse getFeedbacksByRatingCategory(
            @RequestParam Long barbershopId,
            @RequestParam RatingCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return feedbackService.getFeedbackByRatingCategory(barbershopId, category, page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_USER')")
    @DeleteMapping("/deleteFeedback/{deleteFeedbackId}")
    public ResponseEntity<ApiResponse> deleteFeedback(@PathVariable Long deleteFeedbackId) {
        ApiResponse apiResponse = feedbackService.deleteFeedback(deleteFeedbackId);
        return ResponseEntity.ok(apiResponse);
    }
}