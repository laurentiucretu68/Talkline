package com.example.talkline.repository;

import com.example.talkline.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying
    @Query("update User u set u=:user where u.email=:email")
    void updateUser(User user, String email);

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
            " and u.id not in (select id_user1 from friends where id_user2= (select id from users where email=:email))" +
            " and u.id not in (select id_user2 from friends where id_user1= (select id from users where email=:email))" +
            " order by u.subscribe_date desc" +
            " limit 5", nativeQuery = true)
    Collection<User> selectPossibleFriends(String email);

    @Query("select u from User u where u in " +
            "(select f.user1 from Friends f where f.user2.email=:email)" +
            " or u in (select f.user2 from Friends f where f.user1.email=:email)" +
            " or u in (select r.receiverUser from FriendRequest r where r.senderUser.email=:email)" +
            " or u in (select r.senderUser from FriendRequest r where r.receiverUser.email=:email)")
    Collection<User> getFriends(String email);

    @Query(value = "select * from users u " +
            "where locate(lower(u.email), lower(:words)) != 0 " +
            "or locate(lower(u.first_name), lower(:words)) != 0 " +
            "or locate(lower(u.last_name), lower(:words)) != 0 " +
            "or locate(lower(u.phone), lower(:words)) != 0 " +
            "or locate(lower(:words), lower(u.email)) != 0 " +
            "or locate(lower(:words), lower(u.first_name)) != 0 " +
            "or locate(lower(:words), lower(u.last_name)) != 0 " +
            "or locate(lower(:words), lower(u.phone)) != 0 ", nativeQuery = true)
    Collection<User> searchUsers(String words);
}
