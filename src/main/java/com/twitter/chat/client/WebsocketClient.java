package com.twitter.chat.client;

import com.gmail.merikbest2015.configuration.FeignConfiguration;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.gmail.merikbest2015.constants.FeignConstants.USER_SERVICE;
import static com.gmail.merikbest2015.constants.FeignConstants.WEBSOCKET_SERVICE;
import static com.gmail.merikbest2015.constants.PathConstants.API_V1_WEBSOCKET;

@CircuitBreaker(name = WEBSOCKET_SERVICE)
@FeignClient(name = WEBSOCKET_SERVICE, url = "${service.downstream-url.ms-websocket-service}", path = "/" + WEBSOCKET_SERVICE, configuration = FeignConfiguration.class)
public interface WebsocketClient {
    @PostMapping(API_V1_WEBSOCKET)
    void send(@RequestParam("destination") String destination, @RequestBody Object request);

}
