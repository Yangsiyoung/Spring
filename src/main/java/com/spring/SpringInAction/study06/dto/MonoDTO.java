package com.spring.SpringInAction.study06.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MonoDTO implements Serializable {
    private String data1;
    private String data2;
}
