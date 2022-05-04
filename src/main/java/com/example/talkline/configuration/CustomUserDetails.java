package com.example.talkline.configuration;

import com.example.talkline.entities.Location;
import com.example.talkline.entities.User;
import com.example.talkline.repository.LocationRepository;
import com.example.talkline.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public String getFullName(){
        return user.getFirstName() + " " + user.getLastName();
    }

    public String getBirthDate(){
        return user.getBirthDate();
    }
    
    public String getLocation(){
        return user.getLocation().getCity();
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
