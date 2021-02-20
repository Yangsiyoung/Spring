package com.spring.SpringInAction.study04;

import com.spring.SpringInAction.study04.service.EmailService;
import com.spring.SpringInAction.study04.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;
    private final EmailService emailService;

    @GetMapping
    public String login(String id, String password) {
        loginService.login(id, password);
        return "OK";
    }

    @GetMapping("/pull")
    public String pullLoginMessage(String id, String password) {
        emailService.receiveMessage();
        return "OK";
    }
}
