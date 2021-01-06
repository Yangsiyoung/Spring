package com.spring.SpringInAction.tacos.web;

import com.spring.SpringInAction.tacos.domain.order.Order;
import com.spring.SpringInAction.tacos.domain.order.OrderRepository;
import com.spring.SpringInAction.tacos.domain.user.TacoUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Slf4j
@ConfigurationProperties(prefix = "taco.orders")
@RequestMapping("/orders")
@SessionAttributes("order")
@Controller
public class OrderController {

    private OrderRepository orderRepo;
    private int pageSize = 20;
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping("/current")
    public String orderForm(@AuthenticationPrincipal TacoUser tacoUser, @ModelAttribute(name = "order") Order order, Model model) {
        if (order.getDeliveryName() == null) {
            order.setDeliveryName(tacoUser.getFullName());
        }
        if (order.getDeliveryStreet() == null) {
            order.setDeliveryStreet(tacoUser.getStreet());
        }
        if (order.getDeliveryCity() == null) {
            order.setDeliveryCity(tacoUser.getCity());
        }
        if (order.getDeliveryState() == null) {
            order.setDeliveryState(tacoUser.getState());
        }
        if (order.getDeliveryZip() == null) {
            order.setDeliveryZip(tacoUser.getZip());
        }
        model.addAttribute("user", tacoUser);
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order,
                               Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal TacoUser tacoUser) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        orderRepo.save(order);
        // Session 에 들어간 값을 지워줌
        sessionStatus.setComplete();
        order.setTacoUser(tacoUser);

        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/orderForUser")
    public String orderForUser(@AuthenticationPrincipal TacoUser tacoUser, Model model) {
        Pageable pageable = PageRequest.of(0, pageSize);
        model.addAttribute("orders", orderRepo.findByTacoUserOrderByPlacedAtDesc(tacoUser, pageable));
        return ""+pageable.getPageSize();
    }
}
