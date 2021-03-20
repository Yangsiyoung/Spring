package com.spring.SpringInAction.study06.service;

import com.spring.SpringInAction.study06.dto.SignUpRequestDTO;
import com.spring.SpringInAction.study06.dto.SignUpResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    public Mono<SignUpResponseDTO> signUp(Mono<SignUpRequestDTO> signUpRequestDTOMono) {
        return Mono.just(new SignUpResponseDTO("token"));
    }
}
