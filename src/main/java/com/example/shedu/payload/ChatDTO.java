package com.example.shedu.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime createdAt;
    private List<MessageDTO> messages;
}
