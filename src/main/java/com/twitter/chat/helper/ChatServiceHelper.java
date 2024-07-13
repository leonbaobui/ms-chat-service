package com.twitter.chat.helper;

import main.java.com.leon.baobui.dto.response.chat.ChatTweetResponse;
import main.java.com.leon.baobui.dto.response.chat.ChatUserParticipantResponse;
import main.java.com.leon.baobui.exception.ApiRequestException;
import com.twitter.chat.client.TweetClient;
import com.twitter.chat.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static main.java.com.leon.baobui.constants.ErrorMessage.CHAT_PARTICIPANT_BLOCKED;

@Component
@RequiredArgsConstructor
public class ChatServiceHelper {
    private final UserClient userClient;
    private final TweetClient tweetClient;
    public ChatUserParticipantResponse getChatParticipant(Long userId) {
        return userClient.getChatParticipant(userId);
    }

    public ChatTweetResponse getChatTweet(Long tweetId) {
        return tweetClient.getChatTweet(tweetId);
    }

    public void isParticipantBlocked(Long authUserId, Long userId) {
        if (userClient.isUserBlockedByMyProfile(authUserId) || userClient.isMyProfileBlockedByUser(userId)) {
            throw new ApiRequestException(CHAT_PARTICIPANT_BLOCKED, HttpStatus.BAD_REQUEST);
        }
    }
}
