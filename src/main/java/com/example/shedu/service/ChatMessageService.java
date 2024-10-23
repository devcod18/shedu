package com.example.shedu.service;

import com.example.shedu.entity.Chat;
import com.example.shedu.entity.Message;
import com.example.shedu.entity.User;
import com.example.shedu.payload.ApiResponse;
import com.example.shedu.payload.ChatDTO;
import com.example.shedu.payload.MessageDTO;
import com.example.shedu.repository.ChatRepository;
import com.example.shedu.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper = new ModelMapper();

    public ApiResponse getAllChatsByUser(User user, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Chat> chats = chatRepository.findAllBySenderIdOrReceiverId(user.getId(), pageable);
        List<ChatDTO> chatDTOs = chats.stream()
                .map(chat -> modelMapper.map(chat, ChatDTO.class))
                .collect(Collectors.toList());
        return new ApiResponse(chatDTOs);
    }

    public ApiResponse createChat(User sender, User receiver) {
        Chat chat = Chat.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        chatRepository.save(chat);
        ChatDTO chatDTO = modelMapper.map(chat, ChatDTO.class);
        return new ApiResponse(chatDTO);
    }

    public ApiResponse getMessagesByChatId(Long chatId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = messageRepository.findAllByChatId(chatId, pageable);
        List<MessageDTO> messageDTOs = messages.stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
        return new ApiResponse(messageDTOs);
    }

    public ApiResponse createMessage(MessageDTO messageDTO) {
        Message message = modelMapper.map(messageDTO, Message.class);
        message.setRead(false);
        Message savedMessage = messageRepository.save(message);
        MessageDTO savedMessageDTO = modelMapper.map(savedMessage, MessageDTO.class);
        return new ApiResponse(savedMessageDTO);
    }

    public ApiResponse markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setRead(true);
        Message updatedMessage = messageRepository.save(message);
        MessageDTO messageDTO = modelMapper.map(updatedMessage, MessageDTO.class);
        return new ApiResponse(messageDTO);
    }
}