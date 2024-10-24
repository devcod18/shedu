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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BarberShopRepository barbershopRepository;

    @Transactional
    public ApiResponse addFavorite(ReqFavorite reqFavorite, User user) {
        Optional<User> optionalBarber = Optional.empty();
        Optional<Barbershop> optionalBarbershop = Optional.empty();

        if (reqFavorite.getBarberId() != null && reqFavorite.getBarberId() != 0) {
            optionalBarber = userRepository.findById(reqFavorite.getBarberId());
            if (optionalBarber.isEmpty() || !optionalBarber.get().getUserRole().equals("ROLE_BARBER")) {
                return new ApiResponse(ResponseError.NOTFOUND("Barber"));
            }
        }

        if (reqFavorite.getBarbershopId() != null && reqFavorite.getBarbershopId() != 0) {
            optionalBarbershop = barbershopRepository.findById(reqFavorite.getBarbershopId());
            if (optionalBarbershop.isEmpty()) {
                return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
            }
        }

        boolean existsFavorite = favoriteRepository.existsByUserAndBarberAndBarbershop(
                user,
                optionalBarber.orElse(null),
                optionalBarbershop.orElse(null)
        );

        if (existsFavorite) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Favorite"));
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .barber(optionalBarber.orElse(null))
                .barbershop(optionalBarbershop.orElse(null))
                .date(LocalDateTime.now())
                .build();

        favoriteRepository.save(favorite);

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
                .data(responseList)
                .build();

        return new ApiResponse(customPageable);
    }

    @Transactional
    public ApiResponse deleteFavorite(Long id) {
        Favorite favorite = favoriteRepository.findActiveById(id).orElse(null);
        if (favorite == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Favorite"));
        }
        favorite.setDeleted(true);
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
                .date(favorite.getDate().toLocalDate())
                .isDeleted(favorite.isDeleted())
                .build();
    }
}
