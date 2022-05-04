package com.example.talkline.repository;

import com.example.talkline.entities.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
}
