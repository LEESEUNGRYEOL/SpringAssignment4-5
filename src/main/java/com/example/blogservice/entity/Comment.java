package com.example.blogservice.entity;
import com.example.blogservice.dto.CommentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BLOG_ID", nullable = false)
    private Blog blog;

    @ManyToOne
    @JoinColumn (name = "USER_ID",nullable = false)
    private User user;

    @Column(nullable = false)
    private String content;

    // 생성자.
    @Builder
    private Comment (CommentRequestDto commentRequestDto, User user, Blog blog)
    {
        this.content = commentRequestDto.getContent();
        this.user = user;
        this.blog = blog;
    }

    public void update (CommentRequestDto commentRequestDto, User user)
    {
        this.content = commentRequestDto.getContent();
        this.user = user;
    }

    public static Comment of(CommentRequestDto commentRequestDto,User user,Blog blog)
    {
        return Comment.builder()
                .commentRequestDto(commentRequestDto)
                .user(user)
                .blog(blog)
                .build();
    }

}
