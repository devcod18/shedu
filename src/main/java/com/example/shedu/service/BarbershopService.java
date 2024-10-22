package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Days;
import com.example.shedu.entity.User;
import com.example.shedu.entity.WorkDays;
import com.example.shedu.entity.enums.BarbershopRegion;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.*;
import com.example.shedu.payload.req.ReqBarbershop;
import com.example.shedu.payload.req.ReqWorkDays;
import com.example.shedu.payload.res.ResBarbershop;
import com.example.shedu.payload.res.ResWorkDay;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.FileRepository;
import com.example.shedu.repository.UserRepository;
import com.example.shedu.repository.WorkDaysRepository;
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

    private final WorkDaysService workDaysService;

    public ApiResponse save(ReqBarbershop reqBarbershop, User user, BarbershopRegion region) {

        Optional<Barbershop> existingBarbershop = barberShopRepository.findByTitle(reqBarbershop.getTitle());

        if (existingBarbershop.isPresent()) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Barbershop bu nom bilan allaqachon mavjud!"));
        }

        // Yangi barbershop yaratish
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
        return new ApiResponse("Barbershop muvaffaqiyatli yangilandi.");
    }


    public ApiResponse search(String title,BarbershopRegion region) {
        List<Barbershop> barbershops = barberShopRepository.findByTitleContainingIgnoreCase(title,region);
        if(barbershops!=null){
            List<ResBarbershop> list= toResponseBarbershopList(barbershops);
            return new ApiResponse(list);
        }
        return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
    }
    public ApiResponse getByOwner(User user) {
        List<Barbershop> list = barberShopRepository.findByOwner(user.getId());
        if (list == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }
        List<ResBarbershop> dto = toResponseBarbershopList(list);
        List<BarbershopDto> dtos = new ArrayList<>();
        for (ResBarbershop resBarbershop : dto) {
            ResWorkDay workDays = workDaysService.getWorkDays(resBarbershop.getId());
//            if (workDays == null) {
//                return new ApiResponse(ResponseError.NOTFOUND("WorkDays"));
//            }
            dtos.add(BarbershopDto.builder()
                    .workDay(workDays != null ? workDays : null)
                    .barbershop(resBarbershop)
                    .build());
        }

        return new ApiResponse(dtos);
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
                    .build();

            responseList.add(resBarbershop);
        }
        return responseList;
    }
}

