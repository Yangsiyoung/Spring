package com.spring.SpringInAction.study06.web;

import com.spring.SpringInAction.study06.dto.MonoDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/webflux")
@RestController
public class WebFluxController {

    @GetMapping("/mono")
    public Mono<MonoDTO> getMono() {
        return Mono.just(new MonoDTO("data1", "data2"));
    }

    @PostMapping("/mono")
    public Mono<MonoDTO> postMono(@RequestBody Mono<MonoDTO> monoDTO) {
        return monoDTO;
    }
}
