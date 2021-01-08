package com.spring.SpringInAction.tacos.web.order;

import com.spring.SpringInAction.tacos.domain.ingredient.Ingredient;
import com.spring.SpringInAction.tacos.domain.ingredient.IngredientRepository;
import com.spring.SpringInAction.tacos.domain.order.Order;
import com.spring.SpringInAction.tacos.domain.taco.Taco;
import com.spring.SpringInAction.tacos.domain.taco.TacoRepository;
import com.spring.SpringInAction.tacos.domain.user.TacoUser;
import com.spring.SpringInAction.tacos.domain.user.TacoUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(value = "/design", produces = "application/json")
@RequiredArgsConstructor
@RestController
public class DesignTacoController {
    private final TacoRepository tacoRepository;
    //private final EntityLinks entityLinks;

    @GetMapping("/recent")
    public Iterable<Taco> recentTacos() {
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createAt").descending());
        return tacoRepository.findAll(pageRequest).getContent();
    }
}
