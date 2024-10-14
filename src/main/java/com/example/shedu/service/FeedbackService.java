package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Feedback;
import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqFeedback;
import com.example.shedu.payload.res.ResFeedback;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.FeedbackRepository;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final BarberShopRepository barbershopRepository;

    public ApiResponse addFeedback(ReqFeedback reqFeedback) {
        User user = userRepository.findById(reqFeedback.getUserId())
                .orElse(null);
        if (user == null) {
            return new ApiResponse(ResponseError.NOTFOUND("User topilmadi"));
        }

        Barbershop barbershop = barbershopRepository.findById(reqFeedback.getBarbershopId())
                .orElse(null);
        if (barbershop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop topilmadi"));
        }

        if (reqFeedback.getComment() == null || reqFeedback.getComment().trim().isEmpty()) {
            return new ApiResponse(ResponseError.VALIDATION_FAILED("Fikr bo'sh bo'lishi mumkin emas"));
        }

        if (reqFeedback.getRating() < 1 || reqFeedback.getRating() > 5) {
            return new ApiResponse(ResponseError.VALIDATION_FAILED("Reyting 1 dan 5 gacha bo'lishi kerak"));
        }

        Feedback feedback = Feedback.builder()
                .rating(reqFeedback.getRating())
                .comment(reqFeedback.getComment())
                .user(user)
                .barbershop(barbershop)
                .build();

        feedbackRepository.save(feedback);

        ResFeedback resFeedback = ResFeedback.builder()
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .date(feedback.getDate())
                .userId(user.getId())
                .barbershopId(barbershop.getId())
                .build();

        return new ApiResponse(resFeedback);
    }





}
