package com.spring.SpringInAction.study07.dto.response;

import com.spring.SpringInAction.study07.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponseDTO {
    private Long idx;
    private String name;
    private String grade;

    public static StudentResponseDTO fromDomain(Student student) {
        return new StudentResponseDTO(student.getIdx(), student.getName(), student.getGrade());
    }
}
