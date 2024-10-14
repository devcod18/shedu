package com.example.shedu.payload;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String barbershopName;
    private String userRole;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    private Long fileId;
}
