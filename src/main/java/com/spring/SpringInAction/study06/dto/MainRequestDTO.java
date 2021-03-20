package com.spring.SpringInAction.study06.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainRequestDTO {
    public String param1;
    public String param2;

    @Override
    public String toString() {
        return "MainRequestDTO{" +
                "param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                '}';
    }
}
