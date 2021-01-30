package com.spring.SpringInAction.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactorTest {
    @Test
    public void createFluxTest() throws InterruptedException {
        Flux<String> fruitFlux = Flux
                                    .just("Apple", "Orange", "Grape", "Melon", "Strawberry");

        Flux<Integer> integerFlux = Flux.range(1, 100000);
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1)).take(5);
        integerFlux.subscribe(System.out::println);
        Thread.sleep(10000);

        System.out.println("DONE!");

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Melon")
                .expectNext("Strawberry")
                .verifyComplete();
    }
}
