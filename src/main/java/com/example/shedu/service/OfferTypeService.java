package com.example.shedu.service;
import com.example.shedu.entity.OfferType;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.req.ReqOfferType;
import com.example.shedu.payload.res.ResOfferType;
import com.example.shedu.repository.OfferTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OfferTypeService{

    private final OfferTypeRepository offerTypeRepository;


    public ApiResponse save(ReqOfferType reqOfferType) {
       Boolean b=offerTypeRepository.existsByTitle(reqOfferType.getOfferType());
        if(!b){
            OfferType offerType=OfferType.builder()
                    .title(reqOfferType.getOfferType())
                    .build();
            offerTypeRepository.save(offerType);
             return new ApiResponse("Success");
        }
        return new ApiResponse(ResponseError.ALREADY_EXIST("OfferType"));

    }


    public ApiResponse getAll(int size , int page) {
        Page<OfferType> offerTypeList= offerTypeRepository.findAll(PageRequest.of(page, size));
        List<ResOfferType> resOfferTypeList=new ArrayList<>();
        for (OfferType offerType:offerTypeList) {
            ResOfferType resOfferType=new ResOfferType();
            resOfferType.setId(offerType.getId());
            resOfferType.setName(resOfferType.getName());
            resOfferTypeList.add(resOfferType);
        }
        CustomPageable customPageable= CustomPageable.builder()
                .data(resOfferTypeList)
                .totalPage(offerTypeList.getTotalPages())
                .totalElements(offerTypeList.getTotalElements())
                .size(offerTypeList.getSize())
                .page(page)
                .build();

        return new ApiResponse(customPageable);
    }


    public ApiResponse update(ReqOfferType reqOfferType,Long id) {
        OfferType offerType=offerTypeRepository.findById(id).orElse(null);
        if(offerType!=null){
            offerType.setTitle(reqOfferType.getOfferType());
            offerTypeRepository.save(offerType);
        }
        return new ApiResponse("Success");
    }
    private ResOfferType toResOfferType(OfferType offerType){

        return ResOfferType.builder()
                .id(offerType.getId())
                .name(offerType.getTitle())
                .build();

    }

}