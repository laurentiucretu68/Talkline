package com.example.talkline.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer postId;

    @Column(name = "post_text")
    private String text;

    @Column(name = "post_picture")
    private String picture;

    @Column(name = "likes_number")
    private Integer likes;

    @Column(name = "posting_date")
    private String postingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public String formatDate(){
        String delims  = " ";
        String[] tokenString = postingDate.split(delims);
        return tokenString[1] + ' ' + tokenString[0];
    }
}