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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }

        Barbershop barbershop = barbershopRepository.findById(reqFeedback.getBarbershopId())
                .orElse(null);
        if (barbershop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
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

    /*public ApiResponse getOneFeedback(Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElse(null);
        if (feedback == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Feedback"));
        }

        ResFeedback resFeedback = ResFeedback.builder()
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .date(feedback.getDate())
                .userId(feedback.getUser().getId())
                .barbershopId(feedback.getBarbershop().getId())
                .build();

        return new ApiResponse(resFeedback);
    }*/

    public ApiResponse getAllFeedbacks(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Feedback> feedbacks = feedbackRepository.findAll(pageRequest);

        List<ResFeedback> resFeedbacks = feedbacks.getContent()
                .stream().map(feedback -> ResFeedback.builder()
                        .rating(feedback.getRating())
                        .comment(feedback.getComment())
                        .date(feedback.getDate())
                        .userId(feedback.getUser().getId())
                        .barbershopId(feedback.getBarbershop().getId())
                        .build()).collect(Collectors.toList());

        CustomPageable customPageable = CustomPageable.builder()
                .size(feedbacks.getSize())
                .page(feedbacks.getNumber())
                .totalPage(feedbacks.getTotalPages())
                .totalElements(feedbacks.getTotalElements())
                .data(resFeedbacks).build();

        return new ApiResponse(customPageable);
    }

    public ApiResponse deleteFeedback(Long id) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(id);
        if (feedbackOptional.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Feedback"));
        }

        feedbackRepository.delete(feedbackOptional.get());
        return new ApiResponse("Feedback deleted successfully");
    }
}
