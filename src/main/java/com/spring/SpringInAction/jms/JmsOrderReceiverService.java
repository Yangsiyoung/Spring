package com.spring.SpringInAction.jms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@RequiredArgsConstructor
@Service
public class JmsOrderReceiverService {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "tacocloud.order.queue")
    public void receiveOrder(String message) throws JMSException {
        //String message = (String)jmsTemplate.receiveAndConvert("tacocloud.order.queue");
        log.info("##### Receive Message ######" + message);
    }

}
