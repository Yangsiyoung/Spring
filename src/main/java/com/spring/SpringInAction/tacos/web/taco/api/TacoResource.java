package com.spring.SpringInAction.tacos.web.taco.api;

import com.spring.SpringInAction.tacos.domain.ingredient.Ingredient;
import com.spring.SpringInAction.tacos.domain.taco.Taco;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TacoResource extends RepresentationModel<TacoResource> {
    @Getter
    private final String name;
    @Getter
    private final LocalDate createAt;
    @Getter
    private final List<Ingredient> ingredients;

    public TacoResource(Taco taco) {
        this.name = taco.getName();
        this.createAt = taco.getCreateAt();
        this.ingredients = taco.getIngredients();
    }
}
