package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Feedback;
import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqFeedback;
import com.example.shedu.payload.res.ResFeedback;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.FeedbackRepository;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    private final BarberShopRepository barberShopRepository;

    public ApiResponse addFeedback(ReqFeedback reqFeedback) {
        User user = userRepository.findById(reqFeedback.getUserId()).orElse(null);
        if (user == null) {
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }

        Barbershop barbershop = barberShopRepository.findById(reqFeedback.getBarbershopId()).orElse(null);
        if (barbershop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }

        Feedback feedback = Feedback.builder()
                .rating(reqFeedback.getRating())
                .comment(reqFeedback.getComment())
                .user(user)
                .barbershopId(reqFeedback.getBarbershopId())
                .build();

        feedbackRepository.save(feedback);

        return new ApiResponse("Success");
    }

    public ApiResponse getAllFeedbacks(int page, int size, Long barberId, Long barbershopId) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Feedback> feedbacks;
        if (barberId != null) {
            feedbacks = feedbackRepository.findByUserId(barberId, pageRequest);
        } else if (barbershopId != null) {
            feedbacks = feedbackRepository.findByBarbershopId(barbershopId, pageRequest);
        } else {
            feedbacks = feedbackRepository.findAll(pageRequest);
        }

        List<ResFeedback> resFeedbacks = feedbacks.getContent()
                .stream().map(feedback -> ResFeedback.builder()
                        .rating(feedback.getRating())
                        .comment(feedback.getComment())
                        .createdAt(LocalDateTime.now())
                        .userId(feedback.getUser().getId())
                        .barbershopId(feedback.getBarbershopId())
                        .build()).collect(Collectors.toList());

        CustomPageable customPageable = CustomPageable.builder()
                .size(feedbacks.getSize())
                .page(feedbacks.getNumber())
                .totalPage(feedbacks.getTotalPages())
                .totalElements(feedbacks.getTotalElements())
                .data(resFeedbacks).build();

        return new ApiResponse(customPageable);
    }
}