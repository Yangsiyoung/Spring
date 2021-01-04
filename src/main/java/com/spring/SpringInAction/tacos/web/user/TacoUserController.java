package com.spring.SpringInAction.tacos.web.user;

import com.spring.SpringInAction.tacos.dto.request.users.LoginRequestDTO;
import com.spring.SpringInAction.tacos.dto.request.users.SignUpRequestDTO;
import com.spring.SpringInAction.tacos.service.TacoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class TacoUserController {

    private final TacoUserService tacoUserService;

    @GetMapping("/sign-up")
    public String loadSignUpForm() {
        return "/users/sign-up";
    }

    @PostMapping("/sign-up")
    public String processSignUp(@Valid SignUpRequestDTO signUpRequestDTO, Errors errors, Model model) {
        if(errors.hasErrors()) {
            log.info("SignUp Request Data is Invalid: " + signUpRequestDTO);
            return "/error/errors";
        }
        tacoUserService.signUp(signUpRequestDTO);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loadLoginPage() {
        return "/users/login";
    }
}
