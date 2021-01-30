package com.spring.SpringInAction.study02.web;

import org.springframework.web.bind.annotation.*;
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

    @GetMapping()
    public String get(@RequestParam("test") String test) {
        return test;
    }

    @PostMapping()
    public String post(@RequestBody String test) {
        return test;
    }
}
