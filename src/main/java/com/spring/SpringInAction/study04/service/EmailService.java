package com.spring.SpringInAction.study04.service;

import com.spring.SpringInAction.study04.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JmsTemplate jmsTemplate;

    public void receiveMessage() {
        Member member = (Member)jmsTemplate.receiveAndConvert("ysjleader.login.request");
        log.info("########### SEND LOGIN EMAIL (PULLED) ###########");
        log.info(member.getEmail());
    }
    @JmsListener(destination = "ysjleader.login.request")
    public void sendLoginEmail(Member member) {
        log.info("########### SEND LOGIN EMAIL (PUSHED) ###########");
        log.info(member.getEmail());
    }

}
