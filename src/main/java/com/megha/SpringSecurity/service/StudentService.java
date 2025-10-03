package com.megha.SpringSecurity.service;

import com.megha.SpringSecurity.model.Student;
import com.megha.SpringSecurity.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    AuthenticationManager authManager;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student register(Student s) {
        s.setPassword(encoder.encode(s.getPassword()));
        return studentRepo.save(s);
    }

    public String verify(Student s) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(s.getUsername(), s.getPassword()));
        if (auth.isAuthenticated())
            return jwtService.generateToken(s.getUsername());
        return "Failure";

    }
}

