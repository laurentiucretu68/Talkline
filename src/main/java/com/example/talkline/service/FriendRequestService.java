package com.example.talkline.service;

import com.example.talkline.entities.FriendRequest;
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

    public void deleteFriendRequestByEmails(String email1, String email2){
        friendRequestRepository.deleteFriendRequestByEmails(email1, email2);
    }
}
