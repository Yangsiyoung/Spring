package com.spring.SpringInAction.tacos.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes("order")
@RequestMapping("/test")
@Controller
public class OtherController {

    @ResponseBody
    @GetMapping("/sessionAttributes")
    public String getSessionAttributes(Model model) {
        return model.getAttribute("order").toString();
    }
}
