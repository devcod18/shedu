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
import com.example.shedu.repository.UserRepository;
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
    private final UserRepository userRepository;

    private final BarberShopRepository barberShopRepository;

    public ApiResponse addFeedback(ReqFeedback reqFeedback,User user) {
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

    public ApiResponse getFeedbackByRatingCategory(Long barbershopId, RatingCategory category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Feedback> feedbacks;

        switch (category) {
            case LOW -> {
                feedbacks = feedbackRepository.findByBarbershopIdAndRatingLessThanEqual(barbershopId, 2, pageRequest);
                if (feedbacks.isEmpty()) {
                    return new ApiResponse("Past reytingli feedbacklar mavjud emas.");
                } else if (feedbacks.getTotalElements() <= 10) {
                    return new ApiResponse("Past reytingli feedbacklar soni: " + feedbacks.getTotalElements());
                }
            }
            case MEDIUM -> {
                feedbacks = feedbackRepository.findByBarbershopIdAndRating(barbershopId, 3, pageRequest);
                if (feedbacks.isEmpty()) {
                    return new ApiResponse("O'rta reytingli feedbacklar mavjud emas.");
                } else if (feedbacks.getTotalElements() <= 10) {
                    return new ApiResponse("O'rta reytingli feedbacklar soni: " + feedbacks.getTotalElements());
                }
            }
            case HIGH -> {
                feedbacks = feedbackRepository.findByBarbershopIdAndRatingGreaterThanEqual(barbershopId, 4, pageRequest);
                if (feedbacks.isEmpty()) {
                    return new ApiResponse("Yuqori reytingli feedbacklar mavjud emas.");
                } else if (feedbacks.getTotalElements() <= 10) {
                    return new ApiResponse("Yuqori reytingli feedbacklar soni: " + feedbacks.getTotalElements());
                }
            }
            default -> feedbacks = feedbackRepository.findByBarbershopId(barbershopId, pageRequest);
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

    private ResFeedback toResFeedback(Feedback feedback) {
        return ResFeedback.builder()
                .id(feedback.getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .userId(feedback.getUser().getId())
                .barbershopId(feedback.getBarbershopId())
                .build();
    }
}
