package com.example.blogservice.dto;

import com.example.blogservice.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long commentLikeCount;
    @Builder
    private CommentResponseDto(Comment comment, Long commentLikeCount) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.username = comment.getUser().getUsername();
        this.commentLikeCount = commentLikeCount;
    }

    public static CommentResponseDto from(Comment comment, Long commentLikeCount)
    {
        return CommentResponseDto.builder()
                .comment(comment)
                .commentLikeCount(commentLikeCount)
                .build();
    }

    public static CommentResponseDto from(Comment comment)
    {
        return CommentResponseDto.builder()
                .comment(comment)
                .commentLikeCount(0L)
                .build();
    }

}
