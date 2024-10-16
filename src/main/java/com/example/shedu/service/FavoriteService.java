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

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BarberShopRepository barbershopRepository;

    public ApiResponse addFavorite(ReqFavorite reqFavorite) {
        Optional<User> userOptional = userRepository.findById(reqFavorite.getUserId());
        if (userOptional.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }
        User user = userOptional.get();

        Optional<User> barberOptional = userRepository.findById(reqFavorite.getBarberId());
        if (barberOptional.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Barber"));
        }
        User barber = barberOptional.get();

        Optional<Barbershop> barbershopOptional = barbershopRepository.findById(reqFavorite.getBarbershopId());
        if (barbershopOptional.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        Barbershop barbershop = barbershopOptional.get();

        Favorite favourite = Favorite.builder()
                .user(user)
                .barber(barber)
                .barbershop(barbershop)
                .date(reqFavorite.getDate())
                .build();

        favoriteRepository.save(favourite);

        ResFavorite response = ResFavorite.builder()
                .userId(favourite.getUser().getId())
                .userName(favourite.getUser().getFullName())
                .barberId(favourite.getBarber().getId())
                .barberName(favourite.getBarber().getFullName())
                .barbershopId(favourite.getBarbershop().getId())
                .barbershopName(favourite.getBarbershop().getTitle())
                .date(favourite.getDate()).build();
        return new ApiResponse(response);
    }


    /*public ApiResponse getOneFavorite(Long id) {
        Optional<Favorite> favouriteOptional = favoriteRepository.findById(id);
        if (favouriteOptional.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Favorite"));
        }
        Favorite favourite = favouriteOptional.get();

        ResFavorite response = ResFavorite.builder()
                .userId(favourite.getUser().getId())
                .userName(favourite.getUser().getFullName())
                .barberId(favourite.getBarber().getId())
                .barberName(favourite.getBarber().getFullName())
                .barbershopId(favourite.getBarbershop().getId())
                .barbershopName(favourite.getBarbershop().getTitle())
                .date(favourite.getDate()).build();

        return new ApiResponse(response);
    }*/

    public ApiResponse getAllFavorites(int page, int size) {
        Page<Favorite> favouritePage = favoriteRepository.findAll(PageRequest.of(page, size));

        List<ResFavorite> responseList = favouritePage.getContent()
                .stream().map(favourite -> ResFavorite.builder()
                        .userId(favourite.getUser().getId())
                        .userName(favourite.getUser().getFullName())
                        .barberId(favourite.getBarber().getId())
                        .barberName(favourite.getBarber().getFullName())
                        .barbershopId(favourite.getBarbershop().getId())
                        .barbershopName(favourite.getBarbershop().getTitle())
                        .date(favourite.getDate())
                        .build()).toList();

        CustomPageable customPageable = CustomPageable.builder()
                .size(favouritePage.getSize())
                .page(favouritePage.getNumber())
                .totalPage(favouritePage.getTotalPages())
                .totalElements(favouritePage.getTotalElements())
                .data(responseList).build();

        return new ApiResponse(customPageable);
    }

    public ApiResponse deleteFavorite(Long id) {
        Optional<Favorite> favouriteOptional = favoriteRepository.findById(id);
        if (favouriteOptional.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Favorite"));
        }

        favoriteRepository.delete(favouriteOptional.get());
        return new ApiResponse("Favorite deleted successfully");
    }
}