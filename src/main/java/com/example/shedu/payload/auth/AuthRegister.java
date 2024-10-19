package com.example.shedu.payload.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegister {

    @Size(min = 3, max = 15)
    @NotBlank
    private String fullName;

    @Pattern(regexp = "^998([0-9][012345789]|[0-9][125679]|7[01234569])[0-9]{7}$",
            message = "Invalid Uzbekistan phone number")
    private String phoneNumber;
    private String email;
    private Long barbershopId;
    @Size(min = 5, max = 12)
    private String password;
}


