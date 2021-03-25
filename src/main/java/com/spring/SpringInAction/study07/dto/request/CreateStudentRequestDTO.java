package com.spring.SpringInAction.study07.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateStudentRequestDTO {
    private String name;
    private String grade;
}
