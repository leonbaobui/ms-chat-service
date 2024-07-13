package com.twitter.chat.controller.rest;

import com.twitter.chat.dto.response.ChatMessageResponse;
import com.twitter.chat.service.ChatService;
import com.twitter.chat.dto.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gmail.merikbest2015.constants.PathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UI_V1_CHAT)
public class ChatController {
    private final ChatService chatService;

    @GetMapping(CHAT_ID)
    public ResponseEntity<ChatResponse> getChatById(@PathVariable("chatId") Long chatId) {
        return ResponseEntity.ok(chatService.getChatById(chatId));
    }

    @GetMapping(USERS)
    public ResponseEntity<List<ChatResponse>> getUserChats() {
        return ResponseEntity.ok(chatService.getUserChats());
    }

    @GetMapping(CREATE_USER_ID)
    public ResponseEntity<ChatResponse> createChat(
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(chatService.createChat(userId));
    }
}
