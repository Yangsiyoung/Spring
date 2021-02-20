package com.spring.SpringInAction.study04.service;

import com.spring.SpringInAction.study04.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final JmsTemplate jmsTemplate;

    @Value("${login.jms.destination}")
    private String messageDestination;


    public void login(String id, String password) {
        Member member = findUserInfo(id);
        member.comparePassword(password);
        sendLoginEmail(member);

    }

    private Member findUserInfo(String id) {
        return new Member("ysjleader", "1234", "ysjleader@gmail.com");
    }

    private void sendLoginEmail(Member member) {
        jmsTemplate.convertAndSend(messageDestination, member);
    }

}
