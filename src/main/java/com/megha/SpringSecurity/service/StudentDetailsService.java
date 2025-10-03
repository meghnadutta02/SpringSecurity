package com.megha.SpringSecurity.service;

import com.megha.SpringSecurity.model.Student;
import com.megha.SpringSecurity.model.StudentPrincipal;
import com.megha.SpringSecurity.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//Spring Security requires a UserDetails object to hold user credentials and roles.
//
//Student entity is just a plain JPA model and doesn’t implement UserDetails.
//
//So we wrap it inside StudentPrincipal, which Spring understands.
// Used by Spring Security to fetch user details during authentication.
@Service
public class StudentDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Student s = repo.findByUsername(username);
        if (s == null) {
            System.out.println("Student not found.");
            throw new UsernameNotFoundException("Student not found");
        }
        return new StudentPrincipal(s);
    }
}

/*
 * ===== Spring Security Login Flow =====
 *
 * 1. User submits login form to /login with username and password.
 * 2. Spring Security invokes StudentDetailsService.loadUserByUsername().
 * 3. Your StudentDetailsService fetches user from DB and returns a StudentDetails object.
 * 4. AuthenticationProvider (e.g., DaoAuthenticationProvider) compares the input password
 *    with the stored password using the configured PasswordEncoder.
 * 5. If authentication succeeds:
 *      - Spring creates an Authentication object with user's authorities (roles).
 *      - Stores it in the SecurityContext for the current session.
 * 6. User is now logged in and can access secured endpoints based on roles.
 *
 * Note: The StudentDetails object acts as the 'principal' (authenticated user).
 */

