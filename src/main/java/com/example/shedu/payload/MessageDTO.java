package com.example.shedu.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDTO {
    private Long chatId;
    private String text;
    private LocalDateTime createdAt;
    private boolean isRead;
}
