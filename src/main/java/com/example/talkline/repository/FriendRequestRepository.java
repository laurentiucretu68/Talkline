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
    @Query(value = "delete from requests " +
            "where (receiver_id = (select id from users where email=:email1) " +
            "and sender_id = (select id from users where email=:email2)) " +
            "or (sender_id = (select id from users where email=:email1) " +
            "and receiver_id = (select id from users where email=:email2))", nativeQuery = true)
    void deleteFriendRequestByEmails(@Param("email1") String email1, @Param("email2") String email2);

}
