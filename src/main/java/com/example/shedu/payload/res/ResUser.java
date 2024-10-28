package com.example.shedu.payload.res;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResUser {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String role;
    private String email;
    private Long fileId;
}


