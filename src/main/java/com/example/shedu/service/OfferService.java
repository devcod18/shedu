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
import com.example.shedu.repository.OfferRepository;
import com.example.shedu.repository.OfferTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final BarberShopRepository shopRepository;
    private final OfferTypeRepository offerTypeRepository;

    public ApiResponse create(ReqOffer reqOffer) {

        Barbershop barberShop = shopRepository.findById(reqOffer.getBarberShopId())
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
                .anyMatch(offer -> offer.getOfferType().getId().equals(offerType.getId()));
        if (isDuplicate) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Offer"));
        }


        Offer newOffer = Offer.builder()
                .price(reqOffer.getPrice())
                .barberShop(barberShop)
                .offerType(offerType)
                .info(reqOffer.getInfo())
                .build();

        offerRepository.save(newOffer);
        return new ApiResponse("Success");
    }

    public ApiResponse update(ReqOffer reqOffer, Long id) {

        Offer offer = offerRepository.findById(id).orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }


        Barbershop barberShop = shopRepository.findById(reqOffer.getBarberShopId())
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


        offer.setPrice(reqOffer.getPrice());
        offer.setBarberShop(barberShop);
        offer.setOfferType(offerType);
        offer.setInfo(reqOffer.getInfo());

        offerRepository.save(offer);
        return new ApiResponse("Success");
    }

    public ApiResponse getAll(int size, int page) {
        Page<Offer> offers = offerRepository.findAll( PageRequest.of(page, size));
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
    }
     private ResOffer toResponse(Offer offers) {
        return ResOffer.builder()
                .id(offers.getId())
                .price(offers.getPrice())
                .info(offers.getInfo())
                .offerTypeTitle(offers.getOfferType().getTitle())
                .isDeleted(offers.isDeleted())
                .build();
    }// offerlarni response qilib beradi
    public  ApiResponse changeStatus(Long id,boolean status) {
        Offer offer= offerRepository.findById(id).orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }
        offer.setDeleted(status);
        offerRepository.save(offer);
        return new ApiResponse("Success");
    }

   public ResOffer getById(Long id) {
        Offer offer = offerRepository.findByBarberShopId(id);
        if (offer != null) {
            return toResponse(offer);
        }
        return null;
   }

}
