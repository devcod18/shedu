package com.example.shedu.payload.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResUser {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String role;
    private String email;
    private Long fileId;
}


