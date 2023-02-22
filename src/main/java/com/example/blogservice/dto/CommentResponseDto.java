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
    private LocalDateTime createdat;
    private LocalDateTime modifiedat;
    private Long commentLikeCount;
    @Builder
    private CommentResponseDto(Comment comment, Long commentLikeCount) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdat = comment.getCreatedAt();
        this.modifiedat = comment.getModifiedAt();
        this.username = comment.getUser().getUsername();
        this.commentLikeCount = commentLikeCount;
    }

    private CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdat = comment.getCreatedAt();
        this.modifiedat = comment.getModifiedAt();
        this.username = comment.getUser().getUsername();
        this.commentLikeCount = 0L;
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
                .build();
    }

}
