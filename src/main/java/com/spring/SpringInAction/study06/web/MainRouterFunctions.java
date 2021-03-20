package com.spring.SpringInAction.study06.web;

import com.spring.SpringInAction.study06.dto.MainRequestDTO;
import com.spring.SpringInAction.study06.dto.MainResponseDTO;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class MainRouterFunctions {

    public static Mono<ServerResponse> main(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Mono.just("Hello"), String.class);
    }

    public static Mono<ServerResponse> mainPost(ServerRequest serverRequest) {
        Mono<MainRequestDTO> mainRequestDTOMono = serverRequest.bodyToMono(MainRequestDTO.class);
        Mono<MainResponseDTO> mainResponseDTOMono = mainRequestDTOMono.map((mainRequestDTO -> new MainResponseDTO(mainRequestDTO.getParam1(), mainRequestDTO.getParam2())));
        return ServerResponse.ok().body(mainResponseDTOMono, MainRequestDTO.class);
    }

    public static Mono<ServerResponse> mainPostForm(ServerRequest serverRequest) {
        Mono<MainRequestDTO> mainRequestDTOMono = serverRequest.formData().flatMap((stringStringMultiValueMap -> Mono.just(new MainRequestDTO(stringStringMultiValueMap.getFirst("param1"), stringStringMultiValueMap.getFirst("param2")))));
        Mono<MainResponseDTO> mainResponseDTOMono = mainRequestDTOMono.map((mainRequestDTO -> new MainResponseDTO(mainRequestDTO.getParam1(), mainRequestDTO.getParam2())));
        return ServerResponse.ok().body(mainResponseDTOMono, MainRequestDTO.class);
    }

    public static Mono<ServerResponse> mainPostMultipartForm(ServerRequest serverRequest) {
        return null;
    }
}
