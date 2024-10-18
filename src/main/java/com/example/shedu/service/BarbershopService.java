package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarbershopRegion;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqBarbershop;
import com.example.shedu.payload.res.ResBarbershop;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.FileRepository;
import com.example.shedu.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BarbershopService {

    private final BarberShopRepository barberShopRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public ApiResponse save(ReqBarbershop reqBarbershop) {
        if (barberShopRepository.findByTitle(reqBarbershop.getTitle()) != null) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Barbershop"));
        }

        Barbershop barbershop = Barbershop.builder()
                .title(reqBarbershop.getTitle())
                .info(reqBarbershop.getInfo())
                .date(LocalDate.now())
                .email(reqBarbershop.getEmail())
                .latitude(reqBarbershop.getLat())
                .longitude(reqBarbershop.getLng())
                .homeNumber(reqBarbershop.getHomeNumber())
                .barbershopPic(fileRepository.findById(reqBarbershop.getFile_id()).orElse(null))
                .region(BarbershopRegion.valueOf(reqBarbershop.getRegion().toUpperCase())).build();

        barberShopRepository.save(barbershop);
        return new ApiResponse("Success");
    }

    public ApiResponse getAll(int size, int page) {
        Page<Barbershop> barbershopPage = barberShopRepository.findAll(PageRequest.of(page, size));
        List<ResBarbershop> list = toResponseBarbershopList(barbershopPage.getContent());

        CustomPageable customPageable = CustomPageable.builder()
                .data(list)
                .page(page)
                .size(size)
                .totalElements(barbershopPage.getTotalElements())
                .totalPage(barbershopPage.getTotalPages())
                .build();

        return new ApiResponse(customPageable);
    }

    public ApiResponse getOne(Long id) {
        Barbershop barbershop = barberShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ResponseError.NOTFOUND("Barbershop").getMessage()));

        ResBarbershop resBarbershop = ResBarbershop.builder()
                .owner(barbershop.getOwner().getId())
                .title(barbershop.getTitle())
                .email(barbershop.getEmail())
                .homeNumber(barbershop.getHomeNumber())
                .file_id(barbershop.getBarbershopPic() != null ? barbershop.getBarbershopPic().getId() : null)
                .info(barbershop.getInfo())
                .lat(barbershop.getLatitude())
                .lng(barbershop.getLongitude())
                .region(barbershop.getRegion().toString())
                .build();

        return new ApiResponse(resBarbershop);
    }

    public ApiResponse delete(Long id) {
        Barbershop barbershop = barberShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ResponseError.NOTFOUND("Barbershop").getMessage()));

        barbershop.setActive(false);
        return new ApiResponse("Success");
    }

    public ApiResponse update(Long id, ReqBarbershop reqBarbershop) {
        Barbershop barbershop = barberShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ResponseError.NOTFOUND("Barbershop").getMessage()));

        barbershop.setTitle(reqBarbershop.getTitle());
        barbershop.setInfo(reqBarbershop.getInfo());
        barbershop.setEmail(reqBarbershop.getEmail());
        barbershop.setHomeNumber(reqBarbershop.getHomeNumber());
        barbershop.setLatitude(reqBarbershop.getLat());
        barbershop.setLongitude(reqBarbershop.getLng());
        barbershop.setBarbershopPic(fileRepository.findById(reqBarbershop.getFile_id()).orElse(null));
        barbershop.setRegion(BarbershopRegion.valueOf(reqBarbershop.getRegion().toUpperCase()));

        return new ApiResponse("Success");
    }

    public ApiResponse search(String title) {
        List<Barbershop> barbershops = barberShopRepository.findByTitleAndActive(title);
        barbershops.addAll(barberShopRepository.findByRegionAndIsActive(BarbershopRegion.valueOf(title.toUpperCase())));

        List<ResBarbershop> list = toResponseBarbershopList(barbershops);
        return new ApiResponse(list);
    }

    private List<ResBarbershop> toResponseBarbershopList(List<Barbershop> barbershopList) {
        List<ResBarbershop> responseList = new ArrayList<>();
        for (Barbershop barbershop : barbershopList) {
            Optional<User> user = Optional.ofNullable(userRepository.findByIdAndUserRoleAndEnabledTrue
                    (barbershop.getOwner().getId(), UserRole.ROLE_MASTER));

            Long ownerId = user.map(User::getId).orElse(null);

            ResBarbershop resBarbershop = ResBarbershop.builder()
                    .title(barbershop.getTitle())
                    .owner(ownerId)
                    .lat(barbershop.getLatitude())
                    .lng(barbershop.getLongitude())
                    .info(barbershop.getInfo())
                    .email(barbershop.getEmail())
                    .file_id(barbershop.getBarbershopPic() != null ? barbershop.getBarbershopPic().getId() : null)
                    .region(barbershop.getRegion().toString())
                    .homeNumber(barbershop.getHomeNumber())
                    .build();

            responseList.add(resBarbershop);
        }
        return responseList;
    }
}
