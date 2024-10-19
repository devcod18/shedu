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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BarberShopRepository barbershopRepository;

    public ApiResponse addFavorite(ReqFavorite reqFavorite) {
        User user = userRepository.findById(reqFavorite.getUserId()).orElse(null);
        if (user == null) {
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }

        User barber = userRepository.findById(reqFavorite.getBarberId()).orElse(null);
        if (barber == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barber"));
        }

        Barbershop barbershop = barbershopRepository.findById(reqFavorite.getBarbershopId()).orElse(null);
        if (barbershop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }

        Favorite favourite = Favorite.builder()
                .user(user)
                .barber(barber)
                .barbershop(barbershop)
                .date(LocalDate.now())
                .build();

        favoriteRepository.save(favourite);

        return new ApiResponse("Success");
    }

    public ApiResponse getAllFavorites(int page, int size) {
        Page<Favorite> favoritePage = favoriteRepository.findAll(PageRequest.of(page, size));

        List<ResFavorite> responseList = favoritePage.getContent().stream()
                .map(this::toResFavorite)
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
        Favorite favourite = favoriteRepository.findById(id).orElse(null);
        if (favourite == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Favorite"));
        }

        favoriteRepository.delete(favourite);
        return new ApiResponse("Favorite deleted successfully");
    }

    private ResFavorite toResFavorite(Favorite favorite) {
        return ResFavorite.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .userName(favorite.getUser().getFullName())
                .barberId(favorite.getBarber().getId())
                .barberName(favorite.getBarber().getFullName())
                .barbershopId(favorite.getBarbershop().getId())
                .barbershopName(favorite.getBarbershop().getTitle())
                .date(favorite.getDate())
                .build();
    }
}


