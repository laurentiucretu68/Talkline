package com.example.talkline.service;

import com.example.talkline.entities.FriendRequest;
import com.example.talkline.entities.User;
import com.example.talkline.repository.FriendRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    public void addRequest(FriendRequest friendRequest){
        friendRequestRepository.save(friendRequest);
    }
}
