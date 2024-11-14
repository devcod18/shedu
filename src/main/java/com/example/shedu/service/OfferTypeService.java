package com.example.shedu.service;

import com.example.shedu.entity.OfferType;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOfferType;
import com.example.shedu.repository.OfferTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OfferTypeService{

    private final OfferTypeRepository offerTypeRepository;


    public ApiResponse save(ReqOfferType reqOfferType) {
       Boolean b=offerTypeRepository.existsByTitle(reqOfferType.getTitle());
        if(!b){
            OfferType offerType=OfferType.builder()
                    .title(reqOfferType.getTitle())
                    .created(LocalDateTime.now())
                    .prise(reqOfferType.getPrise())
                    .build();
             return new ApiResponse("Success");
        }
        return new ApiResponse(ResponseError.ALREADY_EXIST("OfferType"));

    }


    public ApiResponse getAll(String s) {
        List<OfferType> offerTypes;
        if (s == null) {
            offerTypes = offerTypeRepository.findAll();
        }else {
            offerTypes = offerTypeRepository.findAllByTitle(s);
        }
        return new ApiResponse(offerTypes);
    }


    public ApiResponse update(ReqOfferType reqOfferType,Long id) {
        OfferType offerType=offerTypeRepository.findById(id).orElse(null);
        if(offerType!=null){
            offerType.setTitle(reqOfferType.getTitle());
            offerType.setPrise(reqOfferType.getPrise());
            offerTypeRepository.save(offerType);
        }
        return new ApiResponse("Success");
    }


    public ApiResponse getOne( String s) {
      OfferType offerType=offerTypeRepository.findByTitle(s);
      if(offerType==null){
          return  new ApiResponse(ResponseError.NOTFOUND("OfferType"));
      }
      return new ApiResponse(offerType);
    }
}
