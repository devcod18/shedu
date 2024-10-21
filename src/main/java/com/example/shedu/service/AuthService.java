package com.example.shedu.service;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.BarberRole;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.auth.AuthLogin;
import com.example.shedu.payload.auth.AuthRegister;
import com.example.shedu.payload.auth.ResponseLogin;
import com.example.shedu.repository.UserRepository;
import com.example.shedu.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public ApiResponse login(AuthLogin authLogin) {
        User user = userRepository.findByPhoneNumber(authLogin.getPhoneNumber()).orElse(null);
        if (user == null) {
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }

        if (passwordEncoder.matches(authLogin.getPassword(), user.getPassword())) {
            String token = jwtProvider.generateToken(authLogin.getPhoneNumber());
            ResponseLogin responseLogin = new ResponseLogin(token, user.getUserRole().name(), user.getId());
            return new ApiResponse(responseLogin);
        }

        return new ApiResponse(ResponseError.PASSWORD_DID_NOT_MATCH());
    }

    public ApiResponse register(AuthRegister auth, UserRole role) {

        User byPhoneNumber = userRepository.findByPhoneNumber(auth.getPhoneNumber()).orElse(null);
        if (byPhoneNumber != null) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Phone number"));
        }

        saveUser(auth, role);
        emailSenderService.sendEmail(auth.getEmail(), "Your activation code:",generateFiveDigitNumber().toString());

        return new ApiResponse("Success");
    }


    public ApiResponse adminSaveLibrarian(AuthRegister auth) {

        User byPhoneNumber = userRepository.findByPhoneNumber(auth.getPhoneNumber()).orElse(null);
        if (byPhoneNumber != null) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Phone number"));
        }

        saveUser(auth, UserRole.ROLE_ADMIN);


        return new ApiResponse("Success");
    }


    private void saveUser(AuthRegister auth, UserRole role) {
        User user = User.builder()
                .fullName(auth.getFullName())
                .email(auth.getEmail())
                .phoneNumber(auth.getPhoneNumber())
                .password(passwordEncoder.encode(auth.getPassword()))
                .userRole(role)
                .barbershopId(auth.getBarbershopId())
                .enabled(false)
                .activationCode(generateFiveDigitNumber())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        userRepository.save(user);

    }

    public ApiResponse checkCode(Integer code) {
        User userOptional = userRepository.findByActivationCode(code);
        if (userOptional == null) {
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }

        if (userOptional.getActivationCode().equals(code)) {
            userOptional.setActivationCode(null);
            userOptional.setEnabled(true);
            userRepository.save(userOptional);
            return new ApiResponse("Succes");
        }
        return new ApiResponse(ResponseError.PASSWORD_DID_NOT_MATCH());
    }

    public Integer generateFiveDigitNumber() {
        Random rand = new Random();
        return rand.nextInt(90000) + 10000;
    }
}

