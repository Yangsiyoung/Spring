package com.spring.SpringInAction.study03.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/study")
@RestController
public class StudyController {

    @GetMapping(value = "/produce", produces = "application/json")
    public String produceJson() {
        return "Client accept only application/json";
    }

    @GetMapping(value = "/produce", produces = "application/x-www-form-urlencoded")
    public String produceFormUrlEncoded() {
        return "Client accept only application/x-www-form-urlencoded";
    }

    @PostMapping(value = "/consume", consumes = "application/json")
    public String consumeJson() {
        return "Server accept only application/json";
    }

    @PostMapping(value = "/consume", consumes = "application/x-www-form-urlencoded")
    public String consumesFormUrlEncoded() {
        return "Server accept only application/x-www-form-urlencoded";
    }

    @PostMapping(value = "/produceAndConsume", produces = "application/json", consumes = "application/json")
    public String produceJsonAndConsumeJson() {
        return "Client accept only application/json And Server accept only application/json";
    }
}
