package com.spring.SpringInAction.jms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JmsController {

    private final JmsOrderMessagingService jmsOrderMessagingService;

    @GetMapping("/message/{message}")
    public String message(@PathVariable("message") String message) {
        jmsOrderMessagingService.sendOrder(message);
        log.info(message);
        return message;
    }
}
