package com.example.talkline.configuration;

import com.example.talkline.entities.User;
import com.example.talkline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = service.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("User not found!");
        }
        return new CustomUserDetails(user);
    }
}
