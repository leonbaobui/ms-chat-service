package com.twitter.chat.controller.rest;

import com.twitter.chat.dto.request.ChatMessageRequest;
import com.twitter.chat.dto.response.ChatMessageResponse;
import com.twitter.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gmail.merikbest2015.constants.PathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UI_V1_CHAT)
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping(CHAT_ID_MESSAGES)
    public ResponseEntity<List<ChatMessageResponse>> getChatMessages(@PathVariable("chatId") Long chatId) {
        return ResponseEntity.ok(chatMessageService.getChatMessages(chatId));
    }

    @GetMapping(CHAT_ID_READ_MESSAGES)
    public ResponseEntity<Long> readChatMessages(@PathVariable("chatId") Long chatId) {
        return ResponseEntity.ok(chatMessageService.readChatMessages(chatId));
    }


    @PostMapping(ADD_MESSAGE)
    public ResponseEntity<Void> addMessage(@RequestBody ChatMessageRequest chatMessageRequest) {
        chatMessageService.addMessage(chatMessageRequest);
        return ResponseEntity.ok().build();
    }
}
