package com.example.shedu.service;



import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Offer;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOffer;

import com.example.shedu.payload.res.ResOffer;

import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.OfferTypeRepository;
import com.example.shedu.repository.OffersRepository;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OffersService {

    private final OffersRepository offersRepository;
    private final OfferTypeRepository offerTypeRepository;
    private final BarberShopRepository barberShopRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;


    public ApiResponse create(ReqOffer reqOffers , Long barbershopId) {
        Barbershop barbershop=barberShopRepository.findById(barbershopId).orElse(null);
        Offer offer=offersRepository.findByBarbershopIdAndDeletedIs(barbershopId,false);
        if(null==barbershop){
            return new ApiResponse(ResponseError.NOTFOUND("Barber"));
        } else if (offer!=null){
            return new ApiResponse(ResponseError.NOTFOUND("Offer"));
        }
        offer= Offer.builder()
                .barbershop(barbershop)

                .offerTypes(reqOffers.getOfferTypes())
                .info(reqOffers.getInfo())
                .build();
        offersRepository.save(offer);
        return new ApiResponse("Success");
    }
    public ApiResponse update(ReqOffer reqOffers, Long id,Long barbershopId) {
      Offer offer=offersRepository.findByBarbershopIdAndDeletedIs(barbershopId,false );
      if(offer==null){
          return new ApiResponse(ResponseError.NOTFOUND("Barbershop"));
      }
      offer.setOfferTypes(reqOffers.getOfferTypes());
      offer.setInfo(reqOffers.getInfo());
      offer.setPrise(reqOffers.getPrise());
      return new ApiResponse(offer);
    }

    public ApiResponse isActive(Long id) {
        Offer offer=offersRepository.findByBarbershopId(id);

        if (offer != null) {
            if (!offer.isDeleted()) {
                offer.setDeleted(true);
                return new ApiResponse(offer);
            }
        }
        return new ApiResponse(ResponseError.NOTFOUND("Offer"));
    }
    public ApiResponse getAllOffers() {
       return null;
    }

    public ApiResponse getOffersByBarberShop(Long id,boolean b) {
   return  null;
    }
//    public ApiResponse search(String title, ServiceType serviceType, int size, int page) {
//        Page<Offers> offersPage = offersRepository.search(title,serviceType, PageRequest.of(page, size));
//        List<ResOffers> resOffersList = offersPage.stream()
//                .map(this::toResponse).collect(Collectors.toList());
//        CustomPageable customPageable = CustomPageable.builder()
//                .data(resOffersList)
//                .page(offersPage.getNumber())
//                .totalPage(offersPage.getTotalPages())
//                .totalElements(offersPage.getTotalElements())
//                .build();
//        return new ApiResponse(customPageable);
//    }

    private ResOffer toResponse(Offer offers) {
        return ResOffer.builder()
                .id(offers.getId())
                .info(offers.getInfo())
                .offerTypes(offers.getOfferTypes())
                .build();

    }


}