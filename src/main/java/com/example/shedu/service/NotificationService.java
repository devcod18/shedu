package com.example.shedu.service;

import com.example.shedu.entity.File;
import com.example.shedu.entity.Notification;
import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.IdList;
import com.example.shedu.payload.NotificationDTO;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.payload.res.ResContactNotification;
import com.example.shedu.payload.res.ResNotification;
import com.example.shedu.repository.FileRepository;
import com.example.shedu.repository.NotificationRepository;
import com.example.shedu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public ApiResponse getCountNotification(User user) {
        int count = notificationRepository.countAllByUserIdAndReadFalse(user.getId());
        return new ApiResponse(count);
    }

    public ApiResponse getNotifications(User user) {
        userRepository.findById(user.getId()).orElseThrow(() ->
                new RuntimeException(String.valueOf(ResponseError.NOTFOUND("User")))
        );

        List<Notification> notifications = notificationRepository.findAllByUserId(user.getId());
        if (notifications.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Notifications list"));
        }

        List<NotificationDTO> notificationDTOS = notifications.stream()
                .map(this::toNotificationDTO)
                .collect(Collectors.toList());

        return new ApiResponse(notificationDTOS);
    }

    public ApiResponse adminSendNotification(ResNotification resNotification, Long fileId) {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("User list"));
        }

        File file = (fileId != null && fileId > 0) ?
                fileRepository.findById(fileId).orElse(null) : null;

        if (fileId != null && file == null) {
            return new ApiResponse(ResponseError.NOTFOUND("File"));
        }

        users.forEach(user -> saveNotification(
                user,
                resNotification.getTitle(),
                resNotification.getContent(),
                file != null ? file.getId() : null,
                false
        ));

        return new ApiResponse("Success");
    }

    public ApiResponse contactNotification(ResContactNotification contact) {
        User admin = userRepository.findById(1L).orElseThrow(() ->
                new RuntimeException(String.valueOf(ResponseError.NOTFOUND("Admin")))
        );

        String title = "Foydalanuvchi: " + contact.getName() + " Raqami: " + contact.getPhone();
        String content = "Xabar: " + contact.getMessage();
        saveNotification(admin, title, content, null, true);

        return new ApiResponse("Success");
    }

    public ApiResponse isReadAllNotification(IdList idList) {
        if (idList.getIds().isEmpty()) {
            return new ApiResponse(ResponseError.DEFAULT_ERROR("List bo'sh bo'lmasligi kerak."));
        }

        List<Notification> notifications = notificationRepository.findAllById(idList.getIds());
        notifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });

        return new ApiResponse("Success");
    }

    public ApiResponse notification(User user) {
        User admin = userRepository.findById(1L).orElseThrow(() ->
                new RuntimeException(String.valueOf(ResponseError.NOTFOUND("Admin")))
        );

        String title = "Bildirishnoma!";
        String content = "( " + user.getFullName() + " ) foydalanuvchi sizdan shedu tizimga kirish uchun ruxsat so'ramozda . . .";
        saveNotification(admin, title, content, null, false);

        return new ApiResponse("Success");
    }

    public ApiResponse deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() ->
                new RuntimeException(String.valueOf(ResponseError.NOTFOUND("Notification")))
        );

        notificationRepository.delete(notification);
        return new ApiResponse("Success");
    }

    public ApiResponse getOnlineNotification(User user) {
        userRepository.findById(user.getId()).orElseThrow(() ->
                new RuntimeException(String.valueOf(ResponseError.NOTFOUND("User")))
        );

        List<Notification> notifications = notificationRepository.findAllByUserId(user.getId());
        if (notifications.isEmpty()) {
            return new ApiResponse(ResponseError.NOTFOUND("Notifications list"));
        }

        List<NotificationDTO> notificationDTOS = notifications.stream()
                .skip(Math.max(0, notifications.size() - 10))
                .map(this::toNotificationDTO)
                .collect(Collectors.toList());

        Collections.reverse(notificationDTOS);
        return new ApiResponse(notificationDTOS);
    }

    public void saveNotification(User user, String title, String content, Long fileId, boolean contact) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Tashkent")));
        notification.setUser(user);
        notification.setFile(fileRepository.findById(fileId).orElse(null));
        notification.setContact(contact);
        notificationRepository.save(notification);
    }

    private NotificationDTO toNotificationDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.isRead(),
                notification.getCreatedAt(),
                notification.getUser().getId(),
                Optional.ofNullable(notification.getFile()).map(File::getId).orElse(0L)
        );
    }
}


