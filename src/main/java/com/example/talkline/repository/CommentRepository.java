package com.example.talkline.repository;

import com.example.talkline.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select c from Comment c where c.post.postId=:id")
    Collection<Comment> selectCommentsOfPost(Integer id);
}
