package com.spring.SpringInAction.tacos.domain.taco;

import com.spring.SpringInAction.tacos.domain.ingredient.Ingredient;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, message = "이름은 5글자 이상이어야합니다.")
    @NotNull
    private String name;

    @ManyToMany(targetEntity = Ingredient.class)
    @Size(min = 1, message = "재료는 1가지 이상 선택하서여합니다.")
    private List<Ingredient> ingredients = new ArrayList<>();

    private LocalDate createAt;

    @PrePersist
    void createdAt() {
        this.createAt = LocalDate.now();
    }
}
