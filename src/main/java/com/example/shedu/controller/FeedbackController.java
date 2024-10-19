package com.example.shedu.controller;

import com.example.shedu.entity.enums.RatingCategory;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqFeedback;
import com.example.shedu.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/addFeedback")
    public ResponseEntity<ApiResponse> addFeedback(@RequestBody ReqFeedback reqFeedback) {
        ApiResponse apiResponse = feedbackService.addFeedback(reqFeedback);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/feedbacks/rating")
    public ApiResponse getFeedbacksByRatingCategory(
            @RequestParam Long barbershopId,
            @RequestParam RatingCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return feedbackService.getFeedbackByRatingCategory(barbershopId, category, page, size);
    }


}


