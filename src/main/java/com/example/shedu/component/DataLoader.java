package com.example.shedu.component;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    public void run(String... args)  {
        if (userRepository.count() == 0) {
            User newUser = new User();
            newUser.setFullName("Admin Admin");
            newUser.setPassword(passwordEncoder.encode("root123"));
            newUser.setUserRole(UserRole.ROLE_SUPER_ADMIN);
            newUser.setPhoneNumber("998901234567");
            newUser.setEmail("admin@gmail.com");
            newUser.setCreated(LocalDateTime.now());
            newUser.setEnabled(true);
            newUser.setAccountNonExpired(true);
            newUser.setAccountNonLocked(true);
            newUser.setCredentialsNonExpired(true);
            newUser.setBarbershopId(1L);
            userRepository.save(newUser);
        }
    }
}
