package com.spring.SpringInAction.study02.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/member")
@RestController
public class MemberController {

    @GetMapping("/login")
    public ModelAndView loginPage() {
        ModelAndView loginView = new ModelAndView();
        loginView.setViewName("login");
        return loginView;
    }
}
