package com.example.shedu.service;


import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Offer;
import com.example.shedu.entity.OfferType;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOffer;
import com.example.shedu.payload.res.ResOffer;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.OfferTypeRepository;
import com.example.shedu.repository.OffersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OffersService {

    private final OffersRepository offersRepository;
    private final BarberShopRepository barberShopRepository;
    private final OfferTypeRepository offerTypeRepository;


    public ApiResponse create(ReqOffer reqOffer) {
        Barbershop barberShop = barberShopRepository.findByIdAndActive(reqOffer.getBarberShopId(), true)
                .orElse(null);
        if (barberShop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("BarberShop"));
        }

        // OfferType mavjudligini tekshirish
        OfferType offerType = offerTypeRepository.findById(reqOffer.getOfferTypeId())
                .orElse(null);
        if (offerType == null) {
            return new ApiResponse(ResponseError.NOTFOUND("OfferType"));
        }

        // Takroriyligini tekshirish (BarberShopda ayni OfferType mavjud emasligini tekshirish)
        boolean isDuplicate = barberShop.getOffers().stream()
                .anyMatch(offer -> offer.getOfferType().getId().equals(offerType.getId()));
        if (isDuplicate) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Offer"));
        }

        // Yangi Offer yaratish
        Offer newOffer = Offer.builder()
                .price(reqOffer.getPrice())
                .duration(reqOffer.getDuration())
                .barberShop(barberShop)
                .offerType(offerType)
                .info(reqOffer.getInfo())
                .build();

        offersRepository.save(newOffer);
        return new ApiResponse("Success");
    }// Yangi Offer yaratish

    public ApiResponse update(ReqOffer reqOffer, Long id) {
        Offer offer = offersRepository.findById(id).orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }

        Barbershop barberShop = barberShopRepository.findById(reqOffer.getBarberShopId())
                .orElse(null);
        if (barberShop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("BarberShop"));
        }
        OfferType offerType = offerTypeRepository.findById(reqOffer.getOfferTypeId())
                .orElse(null);
        if (offerType == null) {
            return new ApiResponse(ResponseError.NOTFOUND("OfferType"));
        }


        boolean isDuplicate = barberShop.getOffers().stream()
                .anyMatch(existingOffer ->
                        existingOffer.getOfferType().getId().equals(offerType.getId()) &&
                                !existingOffer.getId().equals(id)); // O'zidan boshqa `Offer` larni tekshirish
        if (isDuplicate) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Offer"));
        }

        // Offerni yangilash
        offer.setPrice(reqOffer.getPrice());
        offer.setDuration(reqOffer.getDuration());
        offer.setBarberShop(barberShop);
        offer.setOfferType(offerType);
        offer.setInfo(reqOffer.getInfo());

        offersRepository.save(offer);
        return new ApiResponse("Success");
    }// offerni yangiladi

    public ApiResponse delete(Long id) {
        Optional<Offer> offer = offersRepository.findByIdAndDeletedIs(id, false);
        if (offer.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }
        offer.get().setDeleted(true);
        offersRepository.save(offer.get());
        return new ApiResponse("Success");
    }// offerni o'chiradi

    public ApiResponse getOne(Long id) {
        Optional<Offer> offer = offersRepository.findByIdAndDeletedIs(id, false);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }
        return new ApiResponse(offer.get());
    }// offerlarni qaytaradi

    public ApiResponse getAll(Long barberId, int page, int size) {
        Page<Offer> offers = offersRepository.findAllByBarberShopIdAndDeletedIs(barberId, false, PageRequest.of(page, size));
        List<ResOffer> resOffers = offers.stream()
                .map(this::toResponse)
                .toList();

        CustomPageable customPageable = CustomPageable.builder()
                .data(resOffers)
                .page(page)
                .size(size)
                .totalElements(offers.getTotalElements())
                .totalPage(offers.getTotalPages())
                .build();
        return new ApiResponse(customPageable);
    }// barcha BarberShopda offerlarni qaytaradi

    public ApiResponse getAll(int size, int page) {
        Page<Offer> offers = offersRepository.findAllByDeleted(false, PageRequest.of(page, size));
        List<ResOffer> resOffers = offers.stream()
                .map(this::toResponse)
                .toList();
        CustomPageable customPageable = CustomPageable.builder()
                .data(resOffers)
                .page(page)
                .size(size)
                .totalElements(offers.getTotalElements())
                .totalPage(offers.getTotalPages())
                .build();
        return new ApiResponse(customPageable);
    }// barcha offerlarni qaytaradi

    public ApiResponse getAllByOfferType(Long offerTypeId, int page, int size) {
        Page<Offer> offers = offersRepository.findAllByOfferTypeIdAndDeletedIs(offerTypeId, false, PageRequest.of(page, size));
        List<ResOffer> resOffers = offers.stream()
                .map(this::toResponse)
                .toList();
        CustomPageable customPageable = CustomPageable.builder()
                .data(resOffers)
                .page(page)
                .size(size)
                .totalElements(offers.getTotalElements())
                .totalPage(offers.getTotalPages())
                .build();
        return new ApiResponse(customPageable);
    }// barcha OfferType ga tegishli offerlarni qaytaradi


    private ResOffer toResponse(Offer offers) {
        return ResOffer.builder()
                .id(offers.getId())
                .price(offers.getPrice())
                .duration(offers.getDuration())
                .barberShopName(offers.getBarberShop().getTitle())
                .offerTypeTitle(offers.getOfferType().getTitle())
                .isDeleted(offers.isDeleted())
                .build();
    }// offerlarni response qilib beradi
}