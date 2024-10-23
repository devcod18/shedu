
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BarberShopRepository barbershopRepository;

    @Transactional
    public ApiResponse addFavorite(ReqFavorite reqFavorite, User user) {
        User barber = userRepository.findById(reqFavorite.getBarberId()).orElse(null);
        if (barber == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barber"));
        }
        Barbershop barbershop = barbershopRepository.findById(reqFavorite.getBarbershopId()).orElse(null);
        if (barbershop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        boolean exists = favoriteRepository.existsByBarberAndBarbershop(barber, barbershop);
        if (exists) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Favorite"));
        }

        Favorite favourite = Favorite.builder()
                .user(user)
                .barber(barber)
                .barbershop(barbershop)
                .date(LocalDateTime.now())
                .build();

        favoriteRepository.save(favourite);

        return new ApiResponse("success");
    }

    @Transactional
    public ApiResponse getAllFavorites(int page, int size) {
        Page<Favorite> favoritePage = favoriteRepository.findAllActiveSorted(PageRequest.of(page, size));

        List<ResFavorite> responseList = favoritePage.getContent().stream()
                .map(this::toResFavorite)
                .collect(Collectors.toList());

        CustomPageable customPageable = CustomPageable.builder()
                .size(favoritePage.getSize())
                .page(favoritePage.getNumber())
                .totalPage(favoritePage.getTotalPages())
                .totalElements(favoritePage.getTotalElements())
                .data(responseList).build();

        return new ApiResponse(customPageable);
    }

    @Transactional
    public ApiResponse deleteFavorite(Long id) {
        Favorite favourite = favoriteRepository.findActiveById(id).orElse(null);

        if (favourite == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Favorite"));
        }

        favourite.setDeleted(true);
        favoriteRepository.save(favourite);

        return new ApiResponse("success");
    }

    public ResFavorite toResFavorite(Favorite favorite) {
        return ResFavorite.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .userName(favorite.getUser().getFullName())
                .barberId(favorite.getBarber().getId())
                .barberName(favorite.getBarber().getUsername())
                .barbershopId(favorite.getBarbershop().getId())
                .barbershopName(favorite.getBarbershop().getTitle())
                .date(favorite.getDate().toLocalDate())
                .isDeleted(favorite.isDeleted()).build();
    }
}