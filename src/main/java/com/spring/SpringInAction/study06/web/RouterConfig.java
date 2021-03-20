package com.spring.SpringInAction.study06.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static reactor.core.publisher.Mono.just;

@Configuration
public class RouterConfig {

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
}
