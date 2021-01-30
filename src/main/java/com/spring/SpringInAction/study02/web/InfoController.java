package com.spring.SpringInAction.study02.web;

import com.spring.SpringInAction.study02.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequestMapping("/info")
@RestController
public class InfoController {

    @GetMapping("/principal")
    public String principal(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/authentication")
    public String authentication(Authentication authentication) {
        return authentication.getName() + " " + authentication.getAuthorities();
    }

    @GetMapping("/securityContext")
    public String securityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName() + " " + authentication.getAuthorities();
    }

    @GetMapping("/authenticationPrincipal")
    public String authenticationPrincipal(@AuthenticationPrincipal Member user) {
        return user.getUsername() + " " + user.getAuthorities();
    }

}
