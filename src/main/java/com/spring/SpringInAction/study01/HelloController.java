package com.spring.SpringInAction.study01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.Objects;

@Slf4j
@RestController
public class HelloController {
    @GetMapping
    public String hello(@Valid Order order, Errors errors) {
        if (errors.hasErrors()) {
            return Objects.requireNonNull(errors.getFieldError()).getDefaultMessage();
        }
        return order.toString();
    }
}
