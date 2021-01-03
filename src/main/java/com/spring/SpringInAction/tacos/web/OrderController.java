package com.spring.SpringInAction.tacos.web;

import com.spring.SpringInAction.tacos.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RequestMapping("/orders")
@SessionAttributes("order")
@Controller
public class OrderController {

    @GetMapping("/current")
    public String orderForm(Model model) {
        log.info("value in model: " + model.getAttribute("order"));
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors) {
        if(errors.hasErrors()) {
            return "orderForm";
        }
        log.info("ProcessOrder: " + order);
        return "redirect:/";
    }
}
