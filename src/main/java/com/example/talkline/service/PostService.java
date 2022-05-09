package com.example.talkline.service;

import com.example.talkline.entities.Post;
import com.example.talkline.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void addPost(Post post){
        postRepository.save(post);
    }

    public Collection<Post> selectPostsByUserEmail(String email){
        return postRepository.selectPostsByUserEmail(email);
    }
}
