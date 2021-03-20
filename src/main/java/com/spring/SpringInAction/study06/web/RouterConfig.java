package com.spring.SpringInAction.study06.web;

import com.spring.SpringInAction.study06.domain.User;
import com.spring.SpringInAction.study06.dto.SignUpRequestDTO;
import com.spring.SpringInAction.study06.dto.SignUpResponseDTO;
import com.spring.SpringInAction.study06.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static reactor.core.publisher.Mono.just;

@RequiredArgsConstructor
@Configuration
public class RouterConfig {

    private final UserService userService;

    @Bean
    public RouterFunction<?> helloSpringRouter() {
        return route(GET("/functional/main"), new HandlerFunction<ServerResponse>() {
            @Override
            public Mono<ServerResponse> handle(ServerRequest request) {
                return ServerResponse.ok().body(Mono.just("Hello Spring"), String.class);

            }
        });
    }

    @Bean
    public RouterFunction<?> helloRouter() {
        return route(GET("/functional/hello"), request -> ServerResponse.ok().body(just("hello"), String.class));
    }

    @Bean
    public RouterFunction<?> chainRouter() {
        return route(GET("/functional/chain1"), request -> ServerResponse.ok().body(just("chain1"), String.class))
                .andRoute(GET("/functional/chain2"), request -> ServerResponse.ok().body(just("chain2"), String.class));
    }

    @Bean
    public RouterFunction<?> simplePostRouter() {
        return route(POST("/functional/user").and(contentType(MediaType.APPLICATION_JSON)),
                            request -> {
                                Mono<SignUpRequestDTO> signUpRequestDTOMono = request.bodyToMono(SignUpRequestDTO.class);
                                Mono<User> userMono = signUpRequestDTOMono.map((dto) -> User.builder().nickname(dto.getNickname()).email(dto.getEmail()).build());
                                return ServerResponse.ok().body(userMono, User.class);
                            });
    }

    @Bean
    public RouterFunction<?> simplePostRouter2() {
        return route(POST("/functional/user2").and(contentType(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok().body(userService.signUp(request.bodyToMono(SignUpRequestDTO.class)), SignUpResponseDTO.class));
    }

    @Bean
    public RouterFunction<?> mainRouterFunction() {
        //return route(GET(""), this::main).andRoute(POST(""), this::mainPost).andRoute(POST("/form-encoded").and(contentType(MediaType.APPLICATION_FORM_URLENCODED)), this::mainPostForm);
        return route()
                    .GET("", MainRouterFunctions::main)
                    .POST("", contentType(MediaType.APPLICATION_JSON), MainRouterFunctions::mainPost)
                    .POST("", contentType(MediaType.APPLICATION_FORM_URLENCODED), MainRouterFunctions::mainPostForm)
                    //.POST("", contentType(MediaType.MULTIPART_FORM_DATA), MainRouterFunctions::mainPostMultipartForm)
                .build();
    }
}
