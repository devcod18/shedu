package com.example.shedu.payload.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseLogin {
    private String token;
    private String role;
    private Long id;

}


