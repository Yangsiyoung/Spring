package com.spring.SpringInAction.study02.config;

import com.spring.SpringInAction.study02.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/member/**", "/h2-console/**", "/error")
                .permitAll()
                .antMatchers("/info/**")
                .authenticated()
                .anyRequest()
                .authenticated();

        http.formLogin();

        http.csrf().ignoringAntMatchers("/h2-console/**"); // 없으면 h2 console 로그인 안됨
        // <iframe> 혹은 <object> 에서 우리 사이트 페이지를 렌더링 할 수 있는지 여부
        // 나는 h2 console 때문에 동일 사이트에서만 허용하는 정책을 넣어둠
        http.headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService);
    }
}
