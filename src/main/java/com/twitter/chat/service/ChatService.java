package com.twitter.chat.service;

import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.mapper.BasicMapper;
import com.gmail.merikbest2015.util.AuthUtil;
import com.twitter.chat.model.Chat;
import com.twitter.chat.model.ChatParticipant;
import com.twitter.chat.repository.ChatMessageRepository;
import com.twitter.chat.repository.ChatParticipantRepository;
import com.twitter.chat.repository.ChatRepository;
import com.twitter.chat.repository.projection.ChatProjection;
import com.twitter.chat.dto.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.gmail.merikbest2015.constants.ErrorMessage.CHAT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final BasicMapper basicMapper;

    @Transactional(readOnly = true)
    public ChatResponse getChatById(Long chatId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        ChatProjection chatProjection = chatRepository.getChatById(chatId, authUserId, ChatProjection.class)
                .orElseThrow(() -> new ApiRequestException(CHAT_NOT_FOUND, HttpStatus.NOT_FOUND));
        return basicMapper.convertToResponse(chatProjection, ChatResponse.class);

    }

    @Transactional(readOnly = true)
    public List<ChatResponse> getUserChats() {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        List<ChatProjection> chatProjection = chatRepository.getChatByUserId(authUserId);
        return basicMapper.convertToResponseList(chatProjection, ChatResponse.class);
    }

    @Transactional
    public ChatResponse createChat(Long userId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();

        Optional<Chat> chatOptional = chatRepository.getChatByParticipants(authUserId, userId);

        if (chatOptional.isEmpty()) {
            Chat chat = new Chat();
            ChatParticipant chatParticipant1 = new ChatParticipant(authUserId, chat);
            ChatParticipant chatParticipant2 = new ChatParticipant(userId, chat);

            chatRepository.save(chat);
            chatParticipantRepository.save(chatParticipant1);
            chatParticipantRepository.save(chatParticipant2);

            chat.setParticipants(List.of(chatParticipant1, chatParticipant2));

            ChatProjection chatProjection = chatRepository.getChatById(chat.getId());
            return basicMapper.convertToResponse(chatProjection, ChatResponse.class);
        }
        return basicMapper.convertToResponse(
                chatRepository.getChatById(chatOptional.get().getId()),
                ChatResponse.class);
    }

}
