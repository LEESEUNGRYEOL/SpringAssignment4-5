package com.example.blogservice.entity;

import com.example.blogservice.repository.CommentLikeRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID",nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private User user;

    // 생성자
    @Builder
    public CommentLike(Comment comment, User user)
    {
        this.comment = comment;
        this.user = user;
    }




}
