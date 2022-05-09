package com.example.talkline.service;

import com.example.talkline.entities.Friends;
import com.example.talkline.repository.FriendsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    public void addFriendship(Friends friendRequest){
        friendsRepository.save(friendRequest);
    }
}
