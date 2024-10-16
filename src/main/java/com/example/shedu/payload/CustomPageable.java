package com.example.shedu.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CustomPageable {
    private int size;
    private int page;
    private int totalPage;
    private long totalElements;
    private Object data;
}
