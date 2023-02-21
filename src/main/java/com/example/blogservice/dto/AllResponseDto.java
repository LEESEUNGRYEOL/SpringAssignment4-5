package com.example.blogservice.dto;


import com.example.blogservice.entity.Blog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AllResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdat;
    private LocalDateTime modifiedat;
    private Long blogLikeCount;

    private List<CommentResponseDto> commentList = new ArrayList<>();


    @Builder
    public AllResponseDto(Blog blog, List<CommentResponseDto> commentList, Long blogLikeCount) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.createdat = blog.getCreatedAt();
        this.modifiedat = blog.getModifiedAt();
        this.blogLikeCount = blogLikeCount;
        this.commentList = commentList;
    }

}
