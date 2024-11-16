package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Favorite;
import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqFavorite;
import com.example.shedu.payload.res.ResFavorite;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.FavoriteRepository;
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
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BarberShopRepository barbershopRepository;

    public ApiResponse addFavorite(ReqFavorite reqFavorite, User user) {
        User barber = null;
        Barbershop barbershop = null;

        if (reqFavorite.getBarbershopId() != null && reqFavorite.getBarbershopId() != 0) {
            barbershop = barbershopRepository.findById(reqFavorite.getBarbershopId()).orElse(null);
            if (barbershop == null) {
                return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
            }

            if (favoriteRepository.existsByUserAndBarbershop(user, barbershop)) {
                return new ApiResponse(ResponseError.ALREADY_EXIST("Barbershop"));
            }
        }

        if (reqFavorite.getBarberId() != null && reqFavorite.getBarberId() != 0) {
            barber = userRepository.findById(reqFavorite.getBarberId()).orElse(null);
            if (barber == null) {
                return new ApiResponse(ResponseError.NOTFOUND("Barber"));
            }

            if (favoriteRepository.existsByUserAndBarber(user, barber)) {
                return new ApiResponse(ResponseError.ALREADY_EXIST("Barber"));
            }
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .barber(barber)
                .barbershop(barbershop)
                .date(LocalDateTime.now())
                .build();

        favoriteRepository.save(favorite);
        return new ApiResponse("success");
    }

    public ApiResponse getAllFavorites(int page, int size, User user) {
        Page<Favorite> favoritePage = favoriteRepository.findAllByUserOrderByDateDesc(
                user, PageRequest.of(page, size));

        List<ResFavorite> responseList = favoritePage.getContent()
                .stream().map(this::toResFavorite)
                .collect(Collectors.toList());

        CustomPageable customPageable = CustomPageable.builder()
                .size(favoritePage.getSize())
                .page(favoritePage.getNumber())
                .totalPage(favoritePage.getTotalPages())
                .totalElements(favoritePage.getTotalElements())
                .data(responseList)
                .build();

        return new ApiResponse(customPageable);
    }

    public ApiResponse deleteFavorite(Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElse(null);
        if (favorite == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Favorite"));
        }

        favoriteRepository.save(favorite);
        return new ApiResponse("success");
    }

    private ResFavorite toResFavorite(Favorite favorite) {
        return ResFavorite.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .userName(favorite.getUser().getFullName())
                .barberId(favorite.getBarber() != null ? favorite.getBarber().getId() : null)
                .barberName(favorite.getBarber() != null ? favorite.getBarber().getFullName() : null)
                .barbershopId(favorite.getBarbershop() != null ? favorite.getBarbershop().getId() : null)
                .barbershopName(favorite.getBarbershop() != null ? favorite.getBarbershop().getTitle() : null)
                .date(favorite.getDate().toLocalDate()).build();
    }
}