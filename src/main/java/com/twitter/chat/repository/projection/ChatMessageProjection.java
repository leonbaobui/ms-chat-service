package com.twitter.chat.repository.projection;

import com.gmail.merikbest2015.dto.response.chat.ChatTweetResponse;
import com.twitter.chat.dto.response.ChatMessageResponse;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface ChatMessageProjection {
    Long getId();

    String getText();

    LocalDateTime getDate();

    Long getAuthorId();
    @Value("#{target.tweetId == null ? null : @chatServiceHelper.getChatTweet(target.tweetId)}")
    ChatTweetResponse getTweet();

    ChatResponse getChat();

    interface ChatResponse {
        Long getId();
    }
}
