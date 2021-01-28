package com.spring.SpringInAction.study02.service;

import com.spring.SpringInAction.study02.domain.Member;
import com.spring.SpringInAction.study02.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.beans.BeanProperty;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /*
     * 비밀번호 다룰떄는 PasswordEncoder 가 꼭 있어야한다.
     * 원하는 인코더를 Bean 등록해두면 된다.
     */
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        memberRepository.save(Member.builder().nickname("라디").username("ysjleader").password(passwordEncoder.encode("1234")).role("user").build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Not Found"));
        return User.builder().username(findMember.getUsername()).password(findMember.getPassword()).roles(findMember.getRole()).build();
    }
}
