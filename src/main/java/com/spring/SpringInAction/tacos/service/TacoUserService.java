package com.spring.SpringInAction.tacos.service;

import com.spring.SpringInAction.tacos.domain.user.TacoUser;
import com.spring.SpringInAction.tacos.domain.user.TacoUserRepository;
import com.spring.SpringInAction.tacos.dto.request.users.LoginRequestDTO;
import com.spring.SpringInAction.tacos.dto.request.users.SignUpRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TacoUserService implements UserDetailsService {

    private final TacoUserRepository tacoUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<TacoUser> loginUser = tacoUserRepository.findByUsername(username);
        if(!loginUser.isPresent()) {
            log.info("Unknown User: " + username);
            throw new UsernameNotFoundException("Unknown User");
        }
        return loginUser.get();
    }

    public void signUp(SignUpRequestDTO signUpRequestDTO) {
        TacoUser tacoUser = signUpRequestDTO.toUser(passwordEncoder);
        tacoUserRepository.save(tacoUser);
    }
}
