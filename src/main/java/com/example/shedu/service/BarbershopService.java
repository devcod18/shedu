package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Notification;


import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarbershopRegion;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.BarbershopDto;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqBarbershop;
import com.example.shedu.payload.res.ResBarbershop;

import com.example.shedu.payload.res.ResUser;
import com.example.shedu.payload.res.ResWorkDay;
import com.example.shedu.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BarbershopService {

    private final BarberShopRepository barberShopRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final NotificationRepository notificationRepository;
    private final OffersRepository offersRepository;
    private final NotificationService notificationService;

    private final WorkDaysService workDaysService;

    public ApiResponse save(ReqBarbershop reqBarbershop, User user, BarbershopRegion region) {

        Optional<Barbershop> existingBarbershop = barberShopRepository.findByTitle(reqBarbershop.getTitle());

        if (existingBarbershop.isPresent()) {
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
                .isActive(true)
                .phoneNumber(user.getPhoneNumber())
                .build();
        barberShopRepository.save(barbershop);
        Notification notification = Notification.builder()
                .title("Succsess")
                .content("Barbershop qo'shildi")
                .read(false)
                .user(user)
                .build();
        notificationRepository.save(notification);
        return new ApiResponse("success");

    }


    public ApiResponse getAll(int size, int page) {
        Page<Barbershop> barbershopPage = barberShopRepository.findAllByActive(PageRequest.of(page, size));
        List<ResBarbershop> list = toResponseBarbershopList(barbershopPage.getContent());
        List<BarbershopDto> dto = barbershopDtos(list);

        CustomPageable customPageable = CustomPageable.builder()
                .data(dto)
                .page(page)
                .size(size)
                .totalElements(barbershopPage.getTotalElements())
                .totalPage(barbershopPage.getTotalPages())
                .build();

        return new ApiResponse(customPageable);
    }

    public ApiResponse delete(Long id) {

        Barbershop barbershop = barberShopRepository.findById(id).orElse(null);
        if (!barbershop.isActive()) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }

        barbershop.setActive(false);
        barberShopRepository.save(barbershop);
        notificationService.saveNotification(
                barbershop.getOwner(),
                "Hurmatli " + barbershop.getOwner().getFullName() + "!",
                "Siz muvaffaqiyatli barbershopni o'chirdingiz",
                0L,
                false
        );
        return new ApiResponse("success");
    }

    public ApiResponse update(User user, ReqBarbershop reqBarbershop, Long barberId,BarbershopRegion region) {
        boolean barbershopExist = barberShopRepository.existsByTitleAndIdNot(reqBarbershop.getTitle(), barberId);
        if (barbershopExist) return new ApiResponse(ResponseError.ALREADY_EXIST("Barbershop"));
        Barbershop barbershop= barberShopRepository.findByIdAndOwnerId(barberId,user.getId());
        if (barbershop==null) return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        barbershop.setInfo(reqBarbershop.getInfo());
        barbershop.setLatitude(reqBarbershop.getLat());
        barbershop.setLongitude(reqBarbershop.getLng());
        barbershop.setAddress(reqBarbershop.getAddress());
        barbershop.setTitle(reqBarbershop.getTitle());
        barbershop.setRegion(region);
        barbershop.setBarbershopPic(fileRepository.findById(reqBarbershop.getFile_id()).orElse(null));
        barberShopRepository.save(barbershop);
        return new ApiResponse("success");
    }

    public ApiResponse search(String title, BarbershopRegion region, int size, int page) {
        Page<Barbershop> barbershops = findBarbershopsBySearchCriteria(title, region, page, size);
        if (barbershops.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }

    List<BarbershopDto> resBarbershopList=new ArrayList<>();
        for (Barbershop barbershop : barbershops) {
            ResWorkDay workDays = workDaysService.getWorkDays(barbershop.getId());
            resBarbershopList.add(BarbershopDto.builder()
                    .workDay(workDays != null ? workDays : null)
                    .barbershop(mapBarbershopToResponse(barbershop))
                    .build());
        }


        return new ApiResponse(createPaginatedResponse(resBarbershopList, barbershops, page, size));
    }

    private Page<Barbershop> findBarbershopsBySearchCriteria(String title, BarbershopRegion region, int page, int size) {
        return barberShopRepository.findByTitleContainingIgnoreCase(title, region, PageRequest.of(page, size));
    }


    private ResBarbershop mapBarbershopToResponse(Barbershop barbershop) {
        List<User> barbers = userRepository.findByBarbershopIdAndUserRoleAndEnabledTrue(barbershop.getId(), UserRole.ROLE_BARBER);
        List<ResUser> resBarberList = mapBarbersToResponse(barbers);

        return ResBarbershop.builder()
                .id(barbershop.getId())
                .title(barbershop.getTitle())
                .owner(barbershop.getOwner().getId())
                .lat(barbershop.getLatitude())
                .lng(barbershop.getLongitude())
                .info(barbershop.getInfo())
                .email(barbershop.getEmail())
                .file_id(barbershop.getBarbershopPic() != null ? barbershop.getBarbershopPic().getId() : null)
                .region(barbershop.getRegion())
                .address(barbershop.getAddress())
                .phone(barbershop.getPhoneNumber())
                .barberList(resBarberList)
                .build();
    }

    private List<ResUser> mapBarbersToResponse(List<User> barbers) {
        return barbers.stream()
                .map(barber -> ResUser.builder()
                        .id(barber.getId())
                        .fullName(barber.getFullName())
                        .email(barber.getEmail())
                        .role(String.valueOf(barber.getUserRole()))
                        .phoneNumber(barber.getPhoneNumber())
                        .build())
                .collect(Collectors.toList());
    }

    private CustomPageable createPaginatedResponse(List<BarbershopDto> resBarbershopList, Page<Barbershop> barbershops, int page, int size) {
        return CustomPageable.builder()
                .data(resBarbershopList)
                .page(page)
                .size(size)
                .totalElements(barbershops.getTotalElements())
                .totalPage(barbershops.getTotalPages())
                .build();
    }
    public ApiResponse getByOwner(User user) {
        List<Barbershop> list = barberShopRepository.findByOwnerOrderByDesc(user.getId());
        if (list == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        List<ResBarbershop> dto = toResponseBarbershopList(list);
        List<BarbershopDto> dtos = barbershopDtos(dto);
        return new ApiResponse(dtos);
    }
    private List<BarbershopDto> barbershopDtos(List<ResBarbershop> barbershopList) {
        List<BarbershopDto> dtos = new ArrayList<>();
        for (ResBarbershop resBarbershop : barbershopList) {
            ResWorkDay workDays = workDaysService.getWorkDays(resBarbershop.getId());

            dtos.add(BarbershopDto.builder()
                    .workDay(workDays != null ? workDays : null)
                    .barbershop(resBarbershop)
                    .build());
        }
        return dtos;

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
                    .region(barbershop.getRegion())
                    .address(barbershop.getAddress())
                    .phone(barbershop.getPhoneNumber())
                    .offerList(barbershop.getOffers())
                    .build();

            responseList.add(resBarbershop);
        }
        return responseList;
    }
}