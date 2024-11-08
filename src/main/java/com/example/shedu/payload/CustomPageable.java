package com.example.shedu.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomPageable {
    private int size;
    private int page;
    private int totalPage;
    private long totalElements;
    private Object data;
}


