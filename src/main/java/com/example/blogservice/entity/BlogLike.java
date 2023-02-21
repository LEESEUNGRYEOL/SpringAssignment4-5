package com.example.blogservice.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class BlogLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BLOG_ID",nullable = false)
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private User user;

    public BlogLike(Blog blog, User user)
    {
        this.blog =blog;
        this.user = user;
    }
}
