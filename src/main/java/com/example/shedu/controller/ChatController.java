package com.example.shedu.controller;

import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.MessageDTO;
import com.example.shedu.repository.UserRepository;
import com.example.shedu.security.CurrentUser;
import com.example.shedu.service.ChatMessageService;
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

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getChatsByUser(@CurrentUser User user,
                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                      @RequestParam(value = "page", defaultValue = "0")int page) {
        ApiResponse allChatsByUser = chatMessageService.getAllChatsByUser(user, size, page);
        return ResponseEntity.ok(allChatsByUser);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createChat(@CurrentUser User user, @RequestParam Long receiver) {
        User receiverUser = userRepository.findById(receiver).orElseThrow(() -> new RuntimeException("Receiver not found"));
        ApiResponse chat = chatMessageService.createChat(user, receiverUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(chat);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<ApiResponse> getMessagesByChat(@PathVariable Long chatId,
                                                         @RequestParam(value = "size", defaultValue = "10")int size,
                                                         @RequestParam(value = "page", defaultValue = "0")int page) {
        ApiResponse messagesByChatId = chatMessageService.getMessagesByChatId(chatId, size, page);
        return ResponseEntity.ok(messagesByChatId);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<ApiResponse> createMessage(@PathVariable Long chatId, @RequestBody MessageDTO messageDTO) {
        messageDTO.setChatId(chatId);
        ApiResponse message = chatMessageService.createMessage(messageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/changeRead/{messageId}")
    public ResponseEntity<ApiResponse> changeRead(@PathVariable Long messageId){
        ApiResponse apiResponse = chatMessageService.markMessageAsRead(messageId);
        return ResponseEntity.ok(apiResponse);
    }
}
