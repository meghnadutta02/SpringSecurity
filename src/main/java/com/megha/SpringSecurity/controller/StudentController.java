package com.megha.SpringSecurity.controller;


import com.megha.SpringSecurity.model.Student;
import com.megha.SpringSecurity.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/register")
    public Student register(@RequestBody Student s) {
        return studentService.register(s);
    }

    @PostMapping("/login")
    public String login(@RequestBody Student s) {
        return studentService.verify(s);
    }

    // by default, Spring Security blocks all POST, PUT, DELETE, and PATCH requests unless they include a valid CSRF token.
    @GetMapping("/csrf-token")

    // Attach the token in headers using the key 'X-CSRF-TOKEN' while making state update requests to the server
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @GetMapping("/")
    public String greet(HttpServletRequest request) {

        return "Hello everyone! Welcome user with session ID: " + request.getSession().getId();
    }

}
