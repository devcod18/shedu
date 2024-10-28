package com.example.shedu.service;

import com.example.shedu.entity.File;
import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.auth.AuthRegister;
import com.example.shedu.payload.res.ResUser;
import com.example.shedu.repository.FileRepository;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileRepository fileRepository;

    public ApiResponse getMe(User user) {
        return new ApiResponse(toResponseUser(user));
    }

    public ApiResponse getAllUsersByRole(int page, int size, UserRole role) {
        var users = userRepository.findAllByUserRole(role, PageRequest.of(page, size));
        List<ResUser> userResponses = users.stream()
                .map(this::toResponseUser)
                .toList();
        return users.getTotalElements() == 0 ?
                new ApiResponse(ResponseError.NOTFOUND("Users")) :
                new ApiResponse(userResponses);
    }


    public ApiResponse updateUser(User user, AuthRegister authRegister) {
        File file = fileRepository.findById(authRegister.getFileId()).orElse(null);
        user.setFullName(authRegister.getFullName());
        user.setFile(file);
        user.setEmail(authRegister.getEmail());
        user.setUpdated(LocalDateTime.now());

        if (authRegister.getPassword() != null && !authRegister.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(authRegister.getPassword()));
        }

        userRepository.save(user);
        return new ApiResponse("success");
    }

    public ApiResponse deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return new ApiResponse("success");
                })
                .orElseGet(() -> new ApiResponse(ResponseError.NOTFOUND("Users")));
    }

    public ApiResponse enableUser(Long id, boolean enabled) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(enabled);
                    userRepository.save(user);
                    return new ApiResponse("success");
                })
                .orElseGet(() -> new ApiResponse(ResponseError.NOTFOUND("Users")));
    }

    public ApiResponse searchUserByRole(String field, UserRole role) {
        List<User> users = userRepository.searchByFieldsAndUserRole(field, role);
        return users.isEmpty() ?
                new ApiResponse(ResponseError.NOTFOUND("Users")) :
                new ApiResponse(users.stream().map(this::toResponseUser).toList());
    }

    private ResUser toResponseUser(User user) {
        return ResUser.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getUserRole().name())
                .build();
    }
}
