package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.MessageDTO;
import com.example.shedu.payload.req.ReqMessage;
import com.example.shedu.repository.UserRepository;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;

    @GetMapping("/myChats")
    @Operation(summary = "Foydalanuvchi bo'yicha chatlarni olish", description = "Foydalanuvchi uchun mavjud chatlarni olish.")
    public ResponseEntity<ApiResponse> getChatsByUser(@CurrentUser User user,
                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                      @RequestParam(value = "page", defaultValue = "0") int page) {
        ApiResponse allChatsByUser = chatMessageService.getAllChatsByUser(user, size, page);
        return ResponseEntity.ok(allChatsByUser);
    }

    @PostMapping("/addChat")
    @Operation(summary = "Yangi chat yaratish", description = "Foydalanuvchi uchun yangi chat yaratish.")
    public ResponseEntity<ApiResponse> createChat(@CurrentUser User user, @RequestParam Long receiver) {
        User receiverUser = userRepository.findById(receiver).orElseThrow(() -> new RuntimeException("Receiver not found"));
        ApiResponse chat = chatMessageService.createChat(user, receiverUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(chat);
    }

    @GetMapping("/{chatId}/messages")
    @Operation(summary = "Chat bo'yicha xabarlarni olish", description = "Berilgan chat ID bo'yicha xabarlarni olish.")
    public ResponseEntity<ApiResponse> getMessagesByChat(@PathVariable Long chatId) {
        ApiResponse messagesByChatId = chatMessageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(messagesByChatId);
    }

    @PostMapping("/{chatId}/addMessages")
    @Operation(summary = "Yangi xabar yaratish", description = "Berilgan chat ID bo'yicha yangi xabar yaratish.")
    public ResponseEntity<ApiResponse> createMessage(@PathVariable Long chatId, @RequestBody ReqMessage messageDTO) {
        ApiResponse message = chatMessageService.createMessage(chatId, messageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/changeRead/{messageId}")
    @Operation(summary = "Xabarni o'qilgan deb belgilash", description = "Berilgan xabar ID bo'yicha xabarni o'qilgan deb belgilash.")
    public ResponseEntity<ApiResponse> changeRead(@PathVariable Long messageId) {
        ApiResponse apiResponse = chatMessageService.markMessageAsRead(messageId);
        return ResponseEntity.ok(apiResponse);
    }
}
