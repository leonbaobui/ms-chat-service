package com.twitter.chat.dto.request;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long chatId;
    private String text;
}
