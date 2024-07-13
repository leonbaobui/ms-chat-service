package com.twitter.chat.service;

import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.mapper.BasicMapper;
import com.gmail.merikbest2015.util.AuthUtil;
import com.twitter.chat.client.WebsocketClient;
import com.twitter.chat.dto.request.ChatMessageRequest;
import com.twitter.chat.dto.response.ChatMessageResponse;
import com.twitter.chat.helper.ChatServiceHelper;
import com.twitter.chat.model.Chat;
import com.twitter.chat.model.ChatMessage;
import com.twitter.chat.repository.ChatMessageRepository;
import com.twitter.chat.repository.ChatParticipantRepository;
import com.twitter.chat.repository.ChatRepository;
import com.twitter.chat.repository.projection.ChatMessageProjection;
import com.twitter.chat.repository.projection.ChatProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gmail.merikbest2015.constants.ErrorMessage.CHAT_NOT_FOUND;
import static com.gmail.merikbest2015.constants.WebsocketConstants.TOPIC_CHAT;

@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final BasicMapper basicMapper;
    private final WebsocketClient websocketClient;
    private final ChatServiceHelper chatServiceHelper;
    public List<ChatMessageResponse> getChatMessages(Long chatId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();

        chatRepository.getChatById(chatId, authUserId, ChatProjection.class)
                .orElseThrow(() -> new ApiRequestException(CHAT_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<ChatMessageProjection> chatMessageProjections = chatMessageRepository.findChatMessagesByChatId(chatId);
        return basicMapper.convertToResponseList(chatMessageProjections, ChatMessageResponse.class);
    }

    @Transactional
    public Long readChatMessages(Long chatId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();

        chatRepository.getChatById(chatId, authUserId, ChatProjection.class)
                .orElseThrow(() -> new ApiRequestException(CHAT_NOT_FOUND, HttpStatus.NOT_FOUND));
        chatMessageRepository.readChatMessages(chatId, authUserId);

        return chatMessageRepository.getUnreadMessagesCount(authUserId);
    }

    //TODO: Need to investigate more on the logic of this flow
    @Transactional
    public void addMessage(ChatMessageRequest chatMessageRequest) {
        ChatMessage chatMessage = basicMapper.convertToResponse(chatMessageRequest, ChatMessage.class);
        Long chatId = chatMessageRequest.getChatId();
        Long authUserId = AuthUtil.getAuthenticatedUserId();

        //verify the chat if it exists
        Chat chat = chatRepository.getChatById(chatId, authUserId, Chat.class)
                .orElseThrow(() -> new ApiRequestException(CHAT_NOT_FOUND, HttpStatus.NOT_FOUND));

        // verify if blocked
        Long participantUserId = chatParticipantRepository.getChatParticipantId(authUserId, chatId);
//        chatServiceHelper.isParticipantBlocked(authUserId, participantUserId);

        //add new msg if everything is okay
        chatMessage.setChat(chat);
        chatMessage.setAuthorId(authUserId);
        chatMessage.setText(chatMessageRequest.getText());
        chatMessageRepository.save(chatMessage);
        chat.getMessages().add(chatMessage);
        ChatMessageProjection message = chatMessageRepository.getChatMessageById(chatMessage.getId()).get();

        // Web-socket to keep connection open during conversation
        Map<Long, ChatMessageProjection> chatMessagePrjMap = chatParticipantRepository.getChatParticipantIds(chatId).stream()
                .collect(Collectors.toMap(Function.identity(), (userId) -> message));
        chatMessagePrjMap.forEach((userId, messageProjection) -> {
            ChatMessageResponse chatMessageResponse = basicMapper.convertToResponse(messageProjection, ChatMessageResponse.class);
            websocketClient.send(TOPIC_CHAT +userId, chatMessageResponse);
        });
    }
}
