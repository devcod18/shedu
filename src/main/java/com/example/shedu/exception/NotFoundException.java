package com.example.shedu.exception;

import com.example.shedu.payload.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import sds_java.shedu3.payload.ApiResponse;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private ApiResponse apiResponse;
    public NotFoundException(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

}
