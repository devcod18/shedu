package com.example.shedu.controller;

import com.example.shedu.entity.User;
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
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/addFeedback")
    public ResponseEntity<ApiResponse> addFeedback(@RequestBody ReqFeedback reqFeedback, @CurrentUser User user) {
        ApiResponse apiResponse = feedbackService.addFeedback(reqFeedback,user);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ROLE_MASTER','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @GetMapping("/getAllFeedbacks")
    public ResponseEntity<ApiResponse> getAllFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long barberId,
            @RequestParam(required = false) Long barbershopId) {
        ApiResponse apiResponse = feedbackService.getAllFeedbacks(page, size, barberId, barbershopId);
        return ResponseEntity.ok(apiResponse);
    }

}
