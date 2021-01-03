package com.spring.SpringInAction.tacos.domain.ingredient;

import lombok.*;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED, force=true)
@Entity
public class Ingredient {

    @Id
    private final String id;
    private final String name;

    @Enumerated(EnumType.STRING)
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
