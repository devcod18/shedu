package com.example.shedu.component;

import com.example.shedu.entity.Days;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.entity.enums.WeekDays;
import com.example.shedu.repository.DaysRepository;
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
    private final DaysRepository daysRepository;

    @Value(value = "${spring.jpa.hibernate.ddl-auto}")

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .fullName("Adminov Adminbek")
                    .password(passwordEncoder.encode("root123"))
                    .userRole(UserRole.ROLE_SUPER_ADMIN)
                    .phoneNumber("998901234567")
                    .email("admin@gmail.com")
                    .created(LocalDateTime.now())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
            userRepository.save(user1);
            User user2 = User.builder()
                    .fullName("Muhammad Nabiyev")
                    .password(passwordEncoder.encode("root123"))
                    .userRole(UserRole.ROLE_MASTER)
                    .phoneNumber("998972911785")
                    .email("mn@gmail.com")
                    .created(LocalDateTime.now())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
            userRepository.save(user2);
            User user3 = User.builder()
                    .fullName("Asilbek Abdihamidov")
                    .password(passwordEncoder.encode("root123"))
                    .userRole(UserRole.ROLE_ADMIN)
                    .phoneNumber("998901234569")
                    .email("asilbek@gmail.com")
                    .created(LocalDateTime.now())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
            userRepository.save(user3);
            User user4 = User.builder()
                    .fullName("Nabiyev Otabek")
                    .password(passwordEncoder.encode("root123"))
                    .userRole(UserRole.ROLE_USER)
                    .phoneNumber("998916368424")
                    .email("otabek@gmail.com")
                    .created(LocalDateTime.now())
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();
            userRepository.save(user3);

            Days days = new Days();
            days.setWeekDays(WeekDays.DUSHANBA);
            daysRepository.save(days);
            Days days1 = new Days();
            days1.setWeekDays(WeekDays.SESHANBA);
            daysRepository.save(days1);
            Days days2 = new Days();
            days2.setWeekDays(WeekDays.CHORSHANBA);
            daysRepository.save(days2);
            Days days3 = new Days();
            days3.setWeekDays(WeekDays.PAYSHANBA);
            daysRepository.save(days3);
            Days days4 = new Days();
            days4.setWeekDays(WeekDays.JUMA);
            daysRepository.save(days4);
            Days days5 = new Days();
            days5.setWeekDays(WeekDays.SHANBA);
            daysRepository.save(days5);
            Days days6 = new Days();
            days6.setWeekDays(WeekDays.YAKSHANBA);
            daysRepository.save(days6);
        }
    }
}

