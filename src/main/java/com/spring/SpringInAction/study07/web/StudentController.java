package com.spring.SpringInAction.study07.web;

import com.spring.SpringInAction.study07.domain.Student;
import com.spring.SpringInAction.study07.domain.StudentRepository;
import com.spring.SpringInAction.study07.dto.request.CreateStudentRequestDTO;
import com.spring.SpringInAction.study07.dto.response.CreateStudentResponseDTO;
import com.spring.SpringInAction.study07.dto.response.StudentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/student")
@RequiredArgsConstructor
@RestController
public class StudentController {
    private final StudentRepository studentRepository;

    @PostMapping
    public Mono<CreateStudentResponseDTO> create(@RequestBody CreateStudentRequestDTO createStudentRequestDTO) {
        Student student = Student.builder().name(createStudentRequestDTO.getName()).grade(createStudentRequestDTO.getGrade()).build();
        Student savedStudent = studentRepository.save(student);
        Mono<Student> studentMono = Mono.just(savedStudent);
        Mono<CreateStudentResponseDTO> result = studentMono.map(CreateStudentResponseDTO::fromDomain);
        return result;
    }

    @GetMapping
    public Flux<StudentResponseDTO> list() {
        List<Student> studentList = studentRepository.findAll();
        Flux<Student> studentFlux = Flux.fromIterable(studentList);
        Flux<StudentResponseDTO> result = studentFlux.map(StudentResponseDTO::fromDomain);
        // flux to Iterable OR List
        // Iterable<StudentResponseDTO> studentResponseDTOIterable = result.toIterable();
        // List<StudentResponseDTO> studentResponseDTOList = result.toStream().collect(Collectors.toList());
        return result;
    }

}
