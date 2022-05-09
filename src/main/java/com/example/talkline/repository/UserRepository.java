package com.example.talkline.repository;

import com.example.talkline.entities.Post;
import com.example.talkline.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select case when count(u)>0 " +
            "then TRUE else false end " +
            "from User u where u.email=:email")
    boolean findUserByEmail(@Param("email") String email);

    @Query("select case when count(u)>0 " +
            "then TRUE else false end " +
            "from User u where u.phone=:phone")
    boolean findUserByPhone(@Param("phone") String phone);

    @Query("select u from User u where u.email=:email")
    User findByEmail(String email);


    @Query("select u from User u where u.userId IN " +
            "(select r.senderUser.userId " +
            "from User u2 join FriendRequest r on u2 = r.receiverUser " +
            "where u2.email=:email)")
    Collection<User> selectFriendRequests(String email);

    @Query(value = "select *" +
            "from users u" +
            " where u.email != :email" +
            " and u.id not in (select sender_id from requests where receiver_id=(select id from users where email=:email))" +
            " and u.id not in (select receiver_id from requests where sender_id=(select id from users where email=:email))" +
            " order by u.subscribe_date desc" +
            " limit 5", nativeQuery = true)
    Collection<User> selectPossibleFriends(String email);
}
