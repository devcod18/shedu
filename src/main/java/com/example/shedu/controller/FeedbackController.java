package com.example.shedu.controller;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.req.ReqFeedback;
import com.example.shedu.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/addFeedback")
    public ResponseEntity<ApiResponse> addFeedback(@RequestBody ReqFeedback reqFeedback) {
        ApiResponse apiResponse = feedbackService.addFeedback(reqFeedback);
        return ResponseEntity.ok(apiResponse);
    }

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
