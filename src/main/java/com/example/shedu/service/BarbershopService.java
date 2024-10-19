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

    public ApiResponse save(ReqBarbershop reqBarbershop, User user, BarbershopRegion region) {
        if (barberShopRepository.findAllByTitleAndActiveTrueAndOwner(reqBarbershop.getTitle(),user) != null) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Barbershop"));
        }

        Barbershop barbershop = Barbershop.builder()
                .title(reqBarbershop.getTitle())
                .info(reqBarbershop.getInfo())
                .owner(user)
                .address(reqBarbershop.getAddress())
                .email(user.getEmail())
                .latitude(reqBarbershop.getLat())
                .longitude(reqBarbershop.getLng())
                .barbershopPic(fileRepository.findById(reqBarbershop.getFile_id()).orElse(null))
                .region(region)
                .build();

        barberShopRepository.save(barbershop);


        return new ApiResponse("Success");
    }

    public ApiResponse getAll(int size, int page) {
        Page<Barbershop> barbershopPage = barberShopRepository.FindAllByActive(PageRequest.of(page, size));
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

    public ApiResponse delete(Long id) {
        Barbershop barbershop = barberShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ResponseError.NOTFOUND("Barbershop").getMessage()));

        barbershop.setActive(false);
        return new ApiResponse("Success");
    }

    public ApiResponse update(Long id, ReqBarbershop reqBarbershop, Long userId, BarbershopRegion region) {
        User user = userRepository.findById(userId).orElse(null);
        Barbershop barbershop = barberShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ResponseError.NOTFOUND("Barbershop").getMessage()));

        if (!barbershop.getOwner().getId().equals(user.getBarbershopId())) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        barbershop.setTitle(reqBarbershop.getTitle());
        barbershop.setInfo(reqBarbershop.getInfo());
        barbershop.setEmail(user.getEmail());
        barbershop.setAddress(reqBarbershop.getAddress());
        barbershop.setLatitude(reqBarbershop.getLat());
        barbershop.setLongitude(reqBarbershop.getLng());
        barbershop.setBarbershopPic(fileRepository.findById(reqBarbershop.getFile_id()).orElse(null));
        barbershop.setRegion(region);
        barberShopRepository.save(barbershop);

        return new ApiResponse("Barbershop muvaffaqiyatli yangilandi.");
    }


    public ApiResponse search(String title, BarbershopRegion region) {
        List<Barbershop> barbershops = barberShopRepository.findByTitleAndRegionAndActive(title, String.valueOf(region));
        barbershops.addAll(barberShopRepository.findByRegionAndIsActive(BarbershopRegion.valueOf(title.toUpperCase())));

        List<ResBarbershop> list = toResponseBarbershopList(barbershops);
        return new ApiResponse(list);

    }
    public ApiResponse GetByOwner(Long id) {
        Barbershop barbershop = barberShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ResponseError.NOTFOUND("Barbershop").getMessage()));
        return new ApiResponse(barbershop);
    }


    private List<ResBarbershop> toResponseBarbershopList(List<Barbershop> barbershopList) {
        List<ResBarbershop> responseList = new ArrayList<>();
        for (Barbershop barbershop : barbershopList) {
            Optional<User> user = Optional.ofNullable(userRepository.findByIdAndUserRoleAndEnabledTrue
                    (barbershop.getOwner().getId(), UserRole.ROLE_MASTER));

            Long ownerId = user.map(User::getId).orElse(null);

            ResBarbershop resBarbershop = ResBarbershop.builder()
                    .id(barbershop.getId())
                    .title(barbershop.getTitle())
                    .owner(ownerId)
                    .lat(barbershop.getLatitude())
                    .lng(barbershop.getLongitude())
                    .info(barbershop.getInfo())
                    .email(barbershop.getEmail())
                    .file_id(barbershop.getBarbershopPic() != null ? barbershop.getBarbershopPic().getId() : null)
                    .region(barbershop.getRegion().toString())
                    .build();

            responseList.add(resBarbershop);
        }
        return responseList;
    }
}

