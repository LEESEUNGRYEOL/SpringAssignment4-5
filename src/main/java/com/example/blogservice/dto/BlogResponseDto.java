package com.example.blogservice.dto;


import com.example.blogservice.entity.Blog;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 문제의 조건에서 반환을 무조건 DTO로만 해달라고 했으므로, 다음과 같이 Response DTO 를 생성해서
// 보내줘야 한다. (아마 이유는 보안상, 즉 비밀번호를 줄 수도 있으므로 이에 대한 보안이라고 생각한다.)
@Getter
@NoArgsConstructor
public class BlogResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long blogLikeCount;

    private List<CommentResponseDto> commentList = new ArrayList<>();

    @Builder
    private BlogResponseDto(Blog blog, List<CommentResponseDto> commentList, Long blogLikeCount) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
        this.blogLikeCount = blogLikeCount;
        this.commentList = commentList;
    }


    public static BlogResponseDto from(Blog blog, List<CommentResponseDto> commentList, Long blogLikeCount) {
        return BlogResponseDto.builder()
                .blog(blog)
                .commentList(commentList)
                .blogLikeCount(blogLikeCount)
                .build();
    }

    public static BlogResponseDto from(Blog blog)
    {
        return BlogResponseDto.builder()
                .blog(blog)
                .blogLikeCount(0L)
                .commentList(new ArrayList<>())
                .build();
    }

}
