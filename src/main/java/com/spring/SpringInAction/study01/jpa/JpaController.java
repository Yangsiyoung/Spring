package com.spring.SpringInAction.study01.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class JpaController {
    private final StudentRepository studentRepository;

    @GetMapping("/student/{name}")
    public String saveStudent(@PathVariable("name") String name) {
        Student savedStudent = studentRepository.save(new Student(name));
        System.out.println(savedStudent.toString());
        return savedStudent.getName();
    }

}
