package com.example.shedu.service;

import com.example.shedu.entity.Chat;
import com.example.shedu.entity.Message;
import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ChatDTO;
import com.example.shedu.payload.MessageDTO;
import com.example.shedu.payload.ResponseError;
import com.example.shedu.repository.ChatRepository;
import com.example.shedu.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    // Convert Chat entity to ChatDTO
    private ChatDTO toResponse(Chat chat) {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setId(chat.getId());
        chatDTO.setSenderId(chat.getSender().getId());
        chatDTO.setReceiverId(chat.getReceiver().getId());
        return chatDTO;
    }

    // Convert Message entity to MessageDTO
    private MessageDTO toResponse(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setChatId(message.getChat().getId());
        messageDTO.setText(message.getText());
        messageDTO.setCreatedAt(message.getCreatedAt());
        messageDTO.setRead(message.isRead());
        return messageDTO;
    }

    public ApiResponse getAllChatsByUser(User user, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Chat> chats = chatRepository.findAllBySenderIdOrReceiverId(user.getId(), pageable);
        List<ChatDTO> chatDTOs = chats.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new ApiResponse(chatDTOs);
    }

    public ApiResponse createChat(User sender, User receiver) {
        if (chatRepository.existsByReceiverId(receiver.getId())) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Chat"));
        }
        Chat chat = Chat.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        chatRepository.save(chat);
        return new ApiResponse("Success");
    }

    public ApiResponse getMessagesByChatId(Long chatId) {
        List<Message> messages = messageRepository.findAllByChatId(chatId);
        List<MessageDTO> messageDTOs = messages.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new ApiResponse(messageDTOs);
    }

    public ApiResponse createMessage(Long chatId, MessageDTO messageDTO) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Chat"));
        }

        Message message = new Message();
        message.setChat(chat);
        message.setText(messageDTO.getText());
        message.setRead(false);
        Message savedMessage = messageRepository.save(message);

        MessageDTO savedMessageDTO = toResponse(savedMessage);
        return new ApiResponse(savedMessageDTO);
    }

    public ApiResponse markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Message"));
        }
        message.setRead(true);
        Message updatedMessage = messageRepository.save(message);

        MessageDTO messageDTO = toResponse(updatedMessage);
        return new ApiResponse(messageDTO);
    }

    public ApiResponse delete(Long messageId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            return new ApiResponse(ResponseError.NOTFOUND("Message"));
        }
        messageRepository.delete(message);
        return new ApiResponse("Success");
    }
}
