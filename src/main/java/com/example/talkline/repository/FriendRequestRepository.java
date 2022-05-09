package com.example.talkline.repository;

import com.example.talkline.entities.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {

    @Modifying
    @Transactional
    @Query("delete from FriendRequest f " +
            "where (f.senderUser.email=:email1 and f.receiverUser.email=:email2) " +
            "or (f.senderUser.email=:email2 and f.receiverUser.email=:email1)")
    void deleteFriendRequestByEmails(@Param("email1") String email1, @Param("email2") String email2);

}
