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

@Service
@RequiredArgsConstructor
@Transactional
public class BarbershopService {

    private final BarberShopRepository barberShopRepository;
    private final UserRepository repository;
    private final FileRepository fileRepository;

    public ApiResponse save(ReqBarbershop reqBarbershop) {
        Barbershop barbershop = barberShopRepository.findByTitle(reqBarbershop.getTitle());
        if (barbershop != null) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Barbershop"));
        }
        Barbershop barbershop1 = Barbershop.builder()
                .title(reqBarbershop.getTitle())
                .info(reqBarbershop.getInfo())
                .date(LocalDate.now())
                .email(reqBarbershop.getEmail())
                .latitude(reqBarbershop.getLat())
                .longitude(reqBarbershop.getLng())
                .homeNumber(reqBarbershop.getHomeNumber())
                .barbershopPic(fileRepository.findById(reqBarbershop.getFile_id()).orElse(null))
                .region(BarbershopRegion.valueOf(reqBarbershop.getRegion().toUpperCase()))
                .build();
        barberShopRepository.save(barbershop1);
        return new ApiResponse("Success");

    }

    public ApiResponse getAll(int size, int page) {
        Page<Barbershop> barbershop = barberShopRepository.findAll(PageRequest.of(page, size));
        List<ResBarbershop> list = toResponseBarbershopList(barbershop.getContent());
        CustomPageable customPageable = CustomPageable.builder()
                .data(list)
                .page(page)
                .size(size)
                .totalElements(barbershop.getTotalElements())
                .totalPage(barbershop.getTotalPages())
                .build();
        return new ApiResponse(customPageable);
    }

    public ApiResponse getOne(Long id) {
        boolean b = barberShopRepository.existsById(id);
        if (!b) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        Barbershop barbershop = barberShopRepository.findById(id).get();
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
        boolean b = barberShopRepository.existsById(id);
        if (!b) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        Barbershop barbershop = barberShopRepository.findById(id).get();
        barbershop.setActive(false);
        return new ApiResponse("Success");
    }

    public ApiResponse update(Long id, ReqBarbershop reqBarbershop) {
        boolean b = barberShopRepository.existsById(id);
        if (!b) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        Barbershop barbershop = barberShopRepository.findById(id).get();
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
        List<Barbershop> barbershop = barberShopRepository.findByTitleAndActive(title);
        List<Barbershop> barbershops =
                barberShopRepository.
                        findByRegionAndIsActive(BarbershopRegion.valueOf(title.toUpperCase()));

        barbershop.addAll(barbershops);
        List<ResBarbershop> list = toResponseBarbershopList(barbershop);
        return new ApiResponse(list);

    }

    private List<ResBarbershop> toResponseBarbershopList(List<Barbershop> barbershop) {
        List<ResBarbershop> list = new ArrayList<>();
        for (Barbershop barbershop1 : barbershop) {
            Long owner = barbershop1.getOwner().getId();
            User user = repository.findByIdAndUserRoleAndEnabledTrue(owner, UserRole.ROLE_MASTER);
            ResBarbershop resBarbershop = ResBarbershop.builder()
                    .title(barbershop1.getTitle())
                    .owner(user.getId())
                    .lat(barbershop1.getLatitude())
                    .lng(barbershop1.getLongitude())
                    .info(barbershop1.getInfo())
                    .email(barbershop1.getEmail())
                    .file_id(barbershop1.getBarbershopPic() != null ? barbershop1.getBarbershopPic().getId() : null)
                    .region(barbershop1.getRegion().toString())
                    .homeNumber(barbershop1.getHomeNumber())
                    .build();
            list.add(resBarbershop);
        }
        return list;
    }

}