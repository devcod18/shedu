package com.example.shedu.service;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.auth.AuthRegister;
import com.example.shedu.payload.res.ResUser;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse getMe(User user) {
        return new ApiResponse(toResponseUser(user));
    }

    public ApiResponse getAllUsersByRole(int page, int size, UserRole role) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepository.findAllByUserRole(role, pageRequest);
        List<ResUser> responseUsers = toResponseUserList(users.getContent());

        if (users.getTotalElements() == 0) {
            return new ApiResponse(ResponseError.NOTFOUND("Foydalanuvchilar"));
        }

        CustomPageable pageable = CustomPageable.builder()
                .page(users.getNumber())
                .size(users.getSize())
                .totalPage(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .data(responseUsers)
                .build();

        return new ApiResponse(pageable);
    }

    public ApiResponse updateUser(User user, AuthRegister authRegister) {
        user.setFullName(authRegister.getFullName());
        user.setPhoneNumber(authRegister.getPhoneNumber());
        user.setUpdated(LocalDateTime.now());

        if (authRegister.getPassword() != null && !authRegister.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(authRegister.getPassword()));
        }

        userRepository.save(user);
        return new ApiResponse("Foydalanuvchi muvaffaqiyatli yangilandi");
    }

    public ApiResponse deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return new ApiResponse("Foydalanuvchi muvaffaqiyatli o'chirildi");
                })
                .orElse(new ApiResponse(ResponseError.NOTFOUND("Foydalanuvchi")));
    }

    public ApiResponse enableUser(Long id, boolean enabled) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Foydalanuvchi"));
        }
        user.setEnabled(enabled);
        userRepository.save(user);
        return new ApiResponse("Foydalanuvchi muvaffaqiyatli o'zgartirildi!");
    }

    public ApiResponse searchUserByRole(String field, UserRole role) {
        List<User> users = userRepository.searchByFieldsAndUserRole(field, role);
        if (users.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Foydalanuvchi"));
        }
        List<ResUser> responseUsers = toResponseUserList(users);
        return new ApiResponse(responseUsers);
    }

    private List<ResUser> toResponseUserList(List<User> users) {
        return users.stream().map(this::toResponseUser).collect(Collectors.toList());
    }

    private ResUser toResponseUser(User user) {
        return ResUser.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(String.valueOf(user.getUserRole()))
                .build();
    }
}

