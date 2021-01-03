package com.spring.SpringInAction.tacos.domain;

import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient,  String> {
}
