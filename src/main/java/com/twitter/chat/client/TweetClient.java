package com.twitter.chat.client;

import com.gmail.merikbest2015.configuration.FeignConfiguration;
import com.gmail.merikbest2015.dto.response.chat.ChatTweetResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.gmail.merikbest2015.constants.FeignConstants.TWEET_SERVICE;
import static com.gmail.merikbest2015.constants.FeignConstants.USER_SERVICE;
import static com.gmail.merikbest2015.constants.PathConstants.API_V1_TWEETS;
import static com.gmail.merikbest2015.constants.PathConstants.CHAT_TWEET_ID;

@CircuitBreaker(name = TWEET_SERVICE)
@FeignClient(name = TWEET_SERVICE, url = "${service.downstream-url.ms-tweet-service}", path ="/" + TWEET_SERVICE + API_V1_TWEETS, contextId = "TweetClient", configuration = FeignConfiguration.class)
public interface TweetClient {
    @GetMapping(CHAT_TWEET_ID)
    ChatTweetResponse getChatTweet(@PathVariable("tweetId") Long tweetId);
}
