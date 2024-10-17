package com.example.shedu.service;

import com.example.shedu.entity.User;
import com.example.shedu.entity.enums.UserRole;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.CustomPageable;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.UserDTO;
import com.example.shedu.payload.auth.AuthRegister;
import com.example.shedu.payload.res.ResUser;
import com.example.shedu.repository.FileRepository;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileRepository fileRepository;

    public ApiResponse getOne(Long id) {
        return userRepository.findById(id)
                .map(ApiResponse::new)
                .orElse(new ApiResponse(ResponseError.NOTFOUND("Foydalanuvchi")));
    }

    public ApiResponse getMe(User user) {
        return new ApiResponse(toResponseUser(user));
    }

    public ApiResponse getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userRepository.findAllByUserRole(UserRole.ROLE_USER, pageRequest);
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

    public ApiResponse updateUser(Long id, AuthRegister authRegister, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Foydalanuvchi"));
        }

        User user = userOptional.get();
        user.setFile(fileRepository.findById(userDTO.getFileId()).orElse(null));
        user.setUpdated(LocalDateTime.now());
        user.setFullName(authRegister.getFullName());
        user.setPhoneNumber(authRegister.getPhoneNumber());

        userRepository.save(user);
        return new ApiResponse("Foydalanuvchi muvaffaqiyatli yangilandi");
    }

    public ApiResponse deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(false);
                    userRepository.save(user);
                    return new ApiResponse("Foydalanuvchi muvaffaqiyatli o'chirildi");
                })
                .orElse(new ApiResponse(ResponseError.NOTFOUND("Foydalanuvchi")));
    }

    public ApiResponse blockUser(Long id, boolean block) {
        User user = userRepository.findById(id).orElse(null);
        user.setEnabled(block);
        userRepository.save(user);
        return new ApiResponse("Success");
    }


    public ApiResponse searchUser(String fullName, String phoneNumber, String email) {// role qushiladi
        List<User> users = userRepository.searchByFields(fullName, phoneNumber, email);
        if (users.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Foydalanuvchi"));
        }
        List<ResUser> responseUsers = toResponseUserList(users);
        return new ApiResponse(responseUsers);
    }

//    public ApiResponse getAdminUsers() {
//        List<User> admins = userRepository.findByUserRole(UserRole.ROLE_ADMIN);
//        if (admins.isEmpty()) {
//            return new ApiResponse(ResponseError.NOTFOUND("Adminlar"));
//        }
//        List<ResUser> responseUsers = toResponseUserList(admins);
//        return new ApiResponse(responseUsers);
//    }

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
