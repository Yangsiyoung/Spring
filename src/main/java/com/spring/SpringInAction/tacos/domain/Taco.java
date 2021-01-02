package com.spring.SpringInAction.tacos.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class Taco {

    @Size(min = 5, message = "이름은 5글자 이상이어야합니다.")
    @NotNull
    private String name;

    @Size(min = 3, message = "재료는 3가지 이상 선택하서여합니다.")
    private List<String> ingredients;
}
