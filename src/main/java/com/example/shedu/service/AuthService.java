package com.example.shedu.service;

import com.example.shedu.entity.File;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarberRole;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.auth.AuthLogin;
import com.example.shedu.payload.auth.AuthRegister;
import com.example.shedu.payload.auth.ResponseLogin;
import com.example.shedu.repository.FileRepository;
import com.example.shedu.repository.UserRepository;
import com.example.shedu.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final NotificationService notificationService;
    private final FileRepository fileRepository;

    public ApiResponse login(AuthLogin authLogin) {
        User user = userRepository.findByPhoneNumber(authLogin.getPhoneNumber()).orElse(null);
        if (user == null) {
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }

        String token = jwtProvider.generateToken(authLogin.getPhoneNumber());
        return new ApiResponse(new ResponseLogin(token, user.getUserRole().name(), user.getId()));
    }

    public ApiResponse register(AuthRegister auth,UserRole role,BarberRole special) {
        if (userRepository.existsByPhoneNumber(auth.getPhoneNumber())) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Phone number"));
        }

        User user = saveUser(auth,role,special);
        emailSenderService.sendEmail(auth.getEmail(), "Your activation code:", user.getActivationCode().toString());

        notificationService.saveNotification(
                user,
                "Hurmatli " + user.getFullName() + "!",
                "Siz muvaffaqiyatli ruyhatdan utdingiz",
                0L,
                false
        );
        return new ApiResponse("Success. Please activate your profile");

    }

    public ApiResponse adminSaveLibrarian(AuthRegister auth) {
        if (userRepository.existsByPhoneNumber(auth.getPhoneNumber())) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Phone number"));
        }

        saveUser(auth,UserRole.ROLE_ADMIN,null);
        return new ApiResponse("Success");
    }

    private User saveUser(AuthRegister auth, UserRole role, BarberRole special) {
        File file = fileRepository.findById(auth.getFileId()).orElse(null);
        User user = User.builder()
                .fullName(auth.getFullName())
                .email(auth.getEmail())
                .phoneNumber(auth.getPhoneNumber())
                .password(passwordEncoder.encode(auth.getPassword()))
                .userRole(role)
                .barbershopId(auth.getBarbershopId())
                .enabled(false)
                .file(file)
                .specials(special)
                .activationCode(generateFiveDigitNumber())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        return userRepository.save(user);
    }

    public ApiResponse checkCode(Integer code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }

        if (!user.getActivationCode().equals(code)) {
            return new ApiResponse(ResponseError.PASSWORD_DID_NOT_MATCH());
        }

        user.setActivationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
        return new ApiResponse("Success");
    }

    private Integer generateFiveDigitNumber() {
        return new Random().nextInt(90000) + 10000;
    }
}
