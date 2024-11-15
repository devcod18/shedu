package com.example.shedu.service;

import com.example.shedu.entity.OfferType;

import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOfferType;
import com.example.shedu.repository.OfferTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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
            offerTypeRepository.save(offerType);
        }
        return new ApiResponse("Success");
    }


    public ApiResponse getOne( Long id) {
      Optional<OfferType> offerType=offerTypeRepository.findById(id);
      if(offerType.isEmpty()){
          return  new ApiResponse(ResponseError.NOTFOUND("OfferType"));
      }
      return new ApiResponse(offerType);
    }
}
