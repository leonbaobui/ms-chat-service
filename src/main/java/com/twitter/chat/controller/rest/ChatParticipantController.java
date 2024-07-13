package com.twitter.chat.controller.rest;

import com.gmail.merikbest2015.dto.response.user.UserResponse;
import com.twitter.chat.dto.request.ChatMessageRequest;
import com.twitter.chat.dto.response.ChatMessageResponse;
import com.twitter.chat.service.ChatParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gmail.merikbest2015.constants.PathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UI_V1_CHAT)
public class ChatParticipantController {
    private final ChatParticipantService chatParticipantService;
    @GetMapping(PARTICIPANT_CHAT_ID)
    public ResponseEntity<UserResponse> getParticipant(@PathVariable("participantId") Long participantId,
                                           @PathVariable("chatId") Long chatId) {
        UserResponse userResponse = chatParticipantService.getParticipant(participantId, chatId);
        return ResponseEntity.ok(userResponse);
    }
}
