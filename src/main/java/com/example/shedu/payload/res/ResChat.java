package com.example.shedu.payload.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResChat {
    private Long id;
    private String receiver;
    private Long fileId;
}
