package com.example.shedu.payload.req;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class ReqMessage {
    private String message;
}
