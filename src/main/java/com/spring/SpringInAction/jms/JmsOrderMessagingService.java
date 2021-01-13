package com.spring.SpringInAction.jms;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JmsOrderMessagingService {

    private final JmsTemplate jmsTemplate;

    public void sendOrder(String message) {
        for(int index = 0; index < 10; index ++)
            jmsTemplate.convertAndSend("tacocloud.order.queue", message);
    }

}
