package com.twitter.chat.service;

import com.gmail.merikbest2015.dto.response.user.UserResponse;
import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.mapper.BasicMapper;
import com.gmail.merikbest2015.util.AuthUtil;
import com.twitter.chat.client.UserClient;
import com.twitter.chat.repository.ChatParticipantRepository;
import com.twitter.chat.repository.ChatRepository;
import com.twitter.chat.repository.projection.UserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.gmail.merikbest2015.constants.ErrorMessage.CHAT_PARTICIPANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatParticipantService {
    private final BasicMapper basicMapper;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRepository chatRepository;
    private final UserClient userClient;
    @Transactional(readOnly = true)
    public UserResponse getParticipant(Long participantId, Long chatId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();

        if(!chatRepository.isChatExists(authUserId, chatId)) {
            throw new ApiRequestException(CHAT_PARTICIPANT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Long userId = chatParticipantRepository.getParticipantUserId(participantId, chatId)
                .orElseThrow(() -> new ApiRequestException(CHAT_PARTICIPANT_NOT_FOUND, HttpStatus.NOT_FOUND));

        return userClient.getUserResponseById(userId);
    }
}
