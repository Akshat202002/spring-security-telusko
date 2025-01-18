package com.akshat.springsecuritytelusko.controllers;

import com.akshat.springsecuritytelusko.model.StudentDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    List<StudentDto> students = new ArrayList<>(List.of(
            new StudentDto(1, "John", 80),
            new StudentDto(2, "Marry", 60)
    ));

    @GetMapping("/students")
    public List<StudentDto> getStudents() {
        return students;
    }

    @PostMapping("/students")
    public StudentDto addStudent(@RequestBody StudentDto studentDto) {
        students.add(studentDto);
        return studentDto;
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
