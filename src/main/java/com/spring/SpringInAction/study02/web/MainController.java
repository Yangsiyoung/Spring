package com.spring.SpringInAction.study02.web;

import com.spring.SpringInAction.study02.config.MyConfData;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MainController {

    private final MyConfData myConfData;

    @GetMapping("/properties")
    public String properties() {
        return myConfData.getHello() + " " + myConfData.getName();
    }
}
