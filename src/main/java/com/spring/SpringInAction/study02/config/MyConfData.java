package com.spring.SpringInAction.study02.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ysjleader")
@Getter
@Setter
@Component
public class MyConfData {
    private String hello; // ysjleader.hello
    private String name; // ysjleader.name
}
