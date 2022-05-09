package com.example.talkline.service;

import com.example.talkline.entities.Comment;
import com.example.talkline.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void addComment(Comment comment){
        commentRepository.save(comment);
    }

    public Collection<Comment> selectCommentsOfPost(Integer id){
        return commentRepository.selectCommentsOfPost(id);
    }
}
