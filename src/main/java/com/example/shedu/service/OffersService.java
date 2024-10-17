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
import org.springframework.data.domain.Pageable;
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

        Offers offers = Offers.builder()
                .barbershop(barbershop)
                .title(reqOffers.getTitle())
                .info(reqOffers.getInfo())
                .price(reqOffers.getPrice())
                .duration(reqOffers.getDuration())
                .build();

        offersRepository.save(offers);

        return new ApiResponse("Success");
    }


    public ApiResponse getAllOffers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Offers> offersPage = offersRepository.findAll(pageable);

        List<ResOffers> resOffersList = offersPage.getContent()
                .stream().map(offer -> ResOffers.builder()
                        .title(offer.getTitle())
                        .info(offer.getInfo())
                        .price(offer.getPrice())
                        .duration(offer.getDuration())
                        .barbershopId(offer.getBarbershop().getId())
                        .build()).collect(Collectors.toList());

        CustomPageable customPageable = CustomPageable.builder()
                .size(offersPage.getSize())
                .page(offersPage.getNumber())
                .totalPage(offersPage.getTotalPages())
                .totalElements(offersPage.getTotalElements())
                .data(resOffersList).build();

        return new ApiResponse(customPageable);
    }


    public ApiResponse updateOffer(Long id, ReqOffers reqOffers) {
        Offers offers = offersRepository.findById(id).orElse(null);
        if (offers == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }

        offers.setTitle(reqOffers.getTitle());
        offers.setInfo(reqOffers.getInfo());
        offers.setPrice(reqOffers.getPrice());
        offers.setDuration(reqOffers.getDuration());

        offersRepository.save(offers);

        return new ApiResponse("Success");
    }


    public ApiResponse deleteOffer(Long id) {
        Offers offers = offersRepository.findById(id).orElse(null);
        if (offers == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }

        offersRepository.delete(offers);
        return new ApiResponse("Success");
    }
}
