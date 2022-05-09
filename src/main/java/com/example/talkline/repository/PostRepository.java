package com.example.talkline.repository;

import com.example.talkline.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "select * " +
            "from posts p " +
            "where user_id = ( " +
            "        select id " +
            "        from users u " +
            "        where u.id = (select id from users u_int where u_int.email = :email) " +
            ") or user_id in ( " +
            "        select id_user1 " +
            "        from friends f " +
            "        where f.id_user1 = (select id from users u_int where u_int.email = :email) " +
            "        or f.id_user2 = (select id from users u_int where u_int.email = :email) " +
            ") or user_id in ( " +
            "        select id_user2 " +
            "        from friends f " +
            "        where f.id_user1 = (select id from users u_int where u_int.email = :email) " +
            "        or f.id_user2 = (select id from users u_int where u_int.email = :email) " +
            "    ) order by id desc ;", nativeQuery = true)
    Collection<Post> selectPostsByUserEmail(String email);
}
