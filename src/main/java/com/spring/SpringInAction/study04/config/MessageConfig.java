package com.spring.SpringInAction.study04.config;

import com.spring.SpringInAction.study04.domain.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MessageConfig {

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTypeIdPropertyName("messageID");
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("loginMessage", Member.class);
        converter.setTypeIdMappings(typeIdMappings);
        return converter;
    }
}
