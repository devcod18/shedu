package com.example.shedu.service;



import com.example.shedu.entity.OfferType;
import com.example.shedu.entity.Offers;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOffers;
import com.example.shedu.payload.res.ResOffers;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.OfferTypeRepository;
import com.example.shedu.repository.OffersRepository;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OffersService {

    private final OffersRepository offersRepository;
    private final OfferTypeRepository offerTypeRepository;
    private final BarberShopRepository barberShopRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;


    public ApiResponse create(ReqOffers reqOffers ,Long barbershopId) {
        boolean b = offersRepository.existsByBarbershopIdAndTitle(barbershopId, reqOffers.getTitle());
        if (!b) {
        List<OfferType> offerTypes = new LinkedList <>();
        for (Long i : reqOffers.getOfferTypes()) {
            OfferType offerType = offerTypeRepository.findById(i).orElse(null);
            offerTypes.add(offerType);
        }
        Offers offers = Offers.builder()
                .title(reqOffers.getTitle())
                .info(reqOffers.getInfo())
                .price(reqOffers.getPrice())
                .duration(reqOffers.getDuration())
                .deleted(false)
                .barbershop(barberShopRepository.findById(barbershopId).get())
                .offerTypes(offerTypes)
                .build();
        offersRepository.save(offers);
        return new ApiResponse("Created");
        }
        return new ApiResponse(ResponseError.ALREADY_EXIST("Offers"));
    }
    public ApiResponse update(ReqOffers reqOffers, Long id, Long barbershopId) {
        Offers offers = offersRepository.findById(id).orElse(null);
        if (offers == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offers"));
        }
        Offers offers1 = offersRepository.findByBarbershopIdAndTitle(barbershopId, reqOffers.getTitle());
        if (offers1 != null && !offers1.getId().equals(id)) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Offers"));
        }
        List<OfferType> offerTypes = new LinkedList <>();
        for (Long i : reqOffers.getOfferTypes()) {
            OfferType offerType = offerTypeRepository.findById(i).orElse(null);
            offerTypes.add(offerType);
        }
        offers.setTitle(reqOffers.getTitle());
        offers.setInfo(reqOffers.getInfo());
        offers.setPrice(reqOffers.getPrice());
        offers.setDuration(reqOffers.getDuration());
        offers.setOfferTypes(offerTypes);
        offers.setBarbershop(barberShopRepository.findById(barbershopId).get());
        offersRepository.save(offers);
        return new ApiResponse("Updated");
    }

    public ApiResponse delete(Long id) {
        Offers offers = offersRepository.findByIdAndDeleted(id);
        if (offers == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offers"));
        }
        offers.setDeleted(true);
        offersRepository.save(offers);
        return new ApiResponse("Deleted");
    }
    public ApiResponse getAllOffers(int page, int size) {
        Page<Offers> offersPage = offersRepository.findByDeletedIsAndOrderByIdDesc(PageRequest.of(page, size));
        List<ResOffers> resOffersList = offersPage.stream()
                .map(this::toResponse).collect(Collectors.toList());
        CustomPageable customPageable = CustomPageable.builder()
                .data(resOffersList)
                .page(offersPage.getNumber())
                .totalPage(offersPage.getTotalPages())
                .totalElements(offersPage.getTotalElements())
                .build();
        return new ApiResponse(customPageable);
    }
    public ApiResponse getOffersByBarberShop(Long id) {
        List<Offers> offers = offersRepository.findByBarbershopIdAndDeleted(id, false);
        List<ResOffers> resOffersList = offers.stream()
                .map(this::toResponse).collect(Collectors.toList());
        if(resOffersList.isEmpty()){
            return new ApiResponse(ResponseError.NOTFOUND("Offers"));
        }
        return new ApiResponse(resOffersList);
    }

    private ResOffers toResponse(Offers offers) {
        return ResOffers.builder()
                .id(offers.getId())
                .title(offers.getTitle())
                .info(offers.getInfo())
                .price(offers.getPrice())
                .duration(offers.getDuration())
                .offerTypes(offers.getOfferTypes())
                .build();
    }
}