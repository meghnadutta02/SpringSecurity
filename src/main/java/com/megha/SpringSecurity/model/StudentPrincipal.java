package com.megha.SpringSecurity.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


// studentPrincipal is the currently authenticated student.
// It comes from Spring Security and wraps the logged-in user's details.
// You can access the original Student object from it.
// studentPrincipal is the authenticated student object that becomes the "principal" stored in the SecurityContext.
public class StudentPrincipal implements UserDetails {
    private Student s;

    public StudentPrincipal(Student s) {
        this.s = s;
    }


//    A user can have multiple roles or permissions.
//    Spring uses this list to check:
//    - What the user is allowed to access (authorization)
//    - Which URLs they can hit
//    - What features they can use


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return s.getPassword();
    }

    @Override
    public String getUsername() {
        return s.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
