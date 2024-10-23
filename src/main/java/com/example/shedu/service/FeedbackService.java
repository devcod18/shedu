package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Feedback;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.RatingCategory;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqFeedback;
import com.example.shedu.payload.res.ResFeedback;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BarberShopRepository barberShopRepository;

    public ApiResponse addFeedback(ReqFeedback reqFeedback, User user) {
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

        return new ApiResponse("success");
    }

    public ApiResponse getFeedbackByRatingCategory(Long barbershopId, RatingCategory category, int page, int size) {
        if (!barberShopRepository.existsById(barbershopId)) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Feedback> feedbacks;

        switch (category) {
            case LOW ->
                    feedbacks = feedbackRepository.findByBarbershopIdAndRatingLessThanEqualAndIsDeletedFalse(barbershopId, 2, pageRequest);
            case MEDIUM ->
                    feedbacks = feedbackRepository.findByBarbershopIdAndRatingAndIsDeletedFalse(barbershopId, 3, pageRequest);
            case HIGH ->
                    feedbacks = feedbackRepository.findByBarbershopIdAndRatingGreaterThanEqualAndIsDeletedFalse(barbershopId, 4, pageRequest);
            case ALL -> feedbacks = feedbackRepository.findByBarbershopIdAndIsDeletedFalse(barbershopId, pageRequest);
            default -> throw new IllegalArgumentException("Unknown rating category: " + category);
        }

        List<ResFeedback> resFeedbacks = feedbacks.getContent()
                .stream().map(this::toResFeedback)
                .collect(Collectors.toList());

        CustomPageable customPageable = CustomPageable.builder()
                .size(feedbacks.getSize())
                .page(feedbacks.getNumber())
                .totalPage(feedbacks.getTotalPages())
                .totalElements(feedbacks.getTotalElements())
                .data(resFeedbacks)
                .build();

        return new ApiResponse(customPageable);
    }


    public ApiResponse deleteFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Feedback"));
        }

        feedback.setDeleted(true);
        feedbackRepository.save(feedback);
        return new ApiResponse("success");
    }

    private ResFeedback toResFeedback(Feedback feedback) {
        return ResFeedback.builder()
                .id(feedback.getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .userId(feedback.getUser().getId())
                .barbershopId(feedback.getBarbershopId())
                .isDeleted(feedback.isDeleted()).build();
    }
}