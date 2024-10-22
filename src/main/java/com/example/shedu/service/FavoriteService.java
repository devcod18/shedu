package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Favorite;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
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

    public ApiResponse addFavorite(Long barberId,Long barbershopId, User user) {

        Favorite favorite = Favorite.builder()
                .user(user)
                .barberId(barberId != null ? barberId:null)
                .date(LocalDate.now())
                .barbershop(barbershopId != null ? barbershopId:null)
                .build();
        favoriteRepository.save(favorite);
        return new ApiResponse("Successfully saved favourite");
    }

    public ApiResponse getAllFavorites(int page, int size) {
        Page<Favorite> favoritePage = favoriteRepository.findAll(PageRequest.of(page, size));

        List<ResFavorite> responseList = favoritePage.getContent().stream()
                .map(favorite -> favorite != null ? toResFavorite(favorite) : null)
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
                .barberId(favorite != null ? favorite.getBarberId() : null)
                .barbershopId(favorite != null ? favorite.getBarbershop() : null)
                .date(favorite.getDate())
                .build();
    }
}


