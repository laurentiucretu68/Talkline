package com.example.talkline.service;

import com.example.talkline.entities.User;
import com.example.talkline.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void addUser(User user){
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public boolean findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public boolean findUserByPhone(String phone){
        return userRepository.findUserByPhone(phone);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Collection<User> selectFriendRequests(String email){
        return userRepository.selectFriendRequests(email);
    }

    public Collection<User> selectPossibleFriends(String email){
        return userRepository.selectPossibleFriends(email);
    }
}
