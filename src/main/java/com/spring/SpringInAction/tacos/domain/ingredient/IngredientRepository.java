package com.spring.SpringInAction.tacos.domain.ingredient;

import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient,  String> {
}
