package com.example.shedu.service;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Offers;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOffers;
import com.example.shedu.payload.res.ResOffers;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.OffersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OffersService {

    private final OffersRepository offersRepository;
    private final BarberShopRepository barberShopRepository;

    public ApiResponse addService(ReqOffers reqOffers) {
        Barbershop barbershop = barberShopRepository.findById(reqOffers.getBarbershopId()).orElse(null);
        if (barbershop == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
        }

        Offers offer = Offers.builder()
                .barbershop(barbershop)
                .title(reqOffers.getTitle())
                .info(reqOffers.getInfo())
                .price(reqOffers.getPrice())
                .duration(reqOffers.getDuration())
                .build();

        offersRepository.save(offer);
        return new ApiResponse("success");
    }

    public ApiResponse getAllOffers(int page, int size) {
        Page<Offers> offersPage = offersRepository.findAllActiveSorted(PageRequest.of(page, size));

        List<ResOffers> resOffersList = offersPage.getContent()
                .stream().map(this::mapToResOffers)
                .collect(Collectors.toList());

        CustomPageable customPageable = CustomPageable.builder()
                .size(offersPage.getSize())
                .page(offersPage.getNumber())
                .totalPage(offersPage.getTotalPages())
                .totalElements(offersPage.getTotalElements())
                .data(resOffersList)
                .build();

        return new ApiResponse(customPageable);
    }

    public ApiResponse updateOffer(Long id, ReqOffers reqOffers) {
        Offers offer = offersRepository.findById(id)
                .orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offers"));
        }

        offer.setTitle(reqOffers.getTitle());
        offer.setInfo(reqOffers.getInfo());
        offer.setPrice(reqOffers.getPrice());
        offer.setDuration(reqOffers.getDuration());

        offersRepository.save(offer);
        return new ApiResponse("success");
    }

    public ApiResponse deleteOffer(Long id) {
        Offers offer = offersRepository.findById(id)
                .orElse(null);
        if (offer == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offers"));
        }

        offer.setDeleted(true);
        offersRepository.save(offer);
        return new ApiResponse("success");
    }

    private ResOffers mapToResOffers(Offers offer) {
        return ResOffers.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .info(offer.getInfo())
                .price(offer.getPrice())
                .duration(offer.getDuration())
                .barbershopId(offer.getBarbershop().getId())
                .deleted(offer.isDeleted()).build();
    }
}