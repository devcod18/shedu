package com.example.shedu.component;

import com.example.shedu.entity.Barbershop;
import com.example.shedu.entity.Days;
import com.example.shedu.entity.OfferType;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.ServiceType;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.entity.enums.WeekDays;
import com.example.shedu.repository.BarberShopRepository;
import com.example.shedu.repository.DaysRepository;
import com.example.shedu.repository.OfferTypeRepository;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DaysRepository daysRepository;
    private final OfferTypeRepository offerTypeRepository;
    private final BarberShopRepository barberShopRepository;

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
            userRepository.save(user4);

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

            OfferType offerType = new OfferType();
            offerType.setTitle("HAIR_CUT");
            offerTypeRepository.save(offerType);
            OfferType offerType1 = new OfferType();
            offerType1.setTitle("HAIR_COLORING");
            offerTypeRepository.save(offerType1);
            OfferType offerType2 = new OfferType();
            offerType2.setTitle("HAIR_STYLING");
            offerTypeRepository.save(offerType2);
            OfferType offerType3 = new OfferType();
            offerType3.setTitle("HAIR_TREATMENT");
            offerTypeRepository.save(offerType3);

            Barbershop barbershop= new Barbershop();
            barbershop.setTitle(" Barbershop");
            barbershop.setAddress("Yekaterinburg, 123 Street");
            barbershop.setBarbershopPic(null);
            barbershop.setInfo("Barber Shop 1");
            barbershop.setLatitude(56.8687);
            barbershop.setLongitude(60.5955);
            barbershop.setOwner(user2);
            barbershop.setPhoneNumber("998901234567");
            barbershop.setEmail("barber1@gmail.com");
            barbershop.setCreated(LocalDate.now());
            barberShopRepository.save(barbershop);

            OfferType offerType= new OfferType();
            offerType.setTitle("Offer Type1");
            offerTypeRepository.save(offerType);
            OfferType offerType1=new OfferType();
            offerType1.setTitle("Offer Type2");
           offerTypeRepository.save(offerType1);

        }
    }
}

