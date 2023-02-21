package com.example.blogservice.controller;


import com.example.blogservice.dto.CommentRequestDto;
import com.example.blogservice.dto.CommentResponseDto;
import com.example.blogservice.dto.MessageResponseDto;
import com.example.blogservice.security.UserDetailsImpl;
import com.example.blogservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    // 0) DI
    private final CommentService commentService;

    // 요구사항1) 댓글 작성 API (POST)
    @PostMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(id, commentRequestDto, userDetails.getUser());
    }

    // 요구사항2) 댓글 수정 API (PUT)
    @PutMapping("/comment/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, commentRequestDto, userDetails.getUser());
    }

    // 요구사항3) 댓글 삭제 API (DEL)
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<MessageResponseDto> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }

    // 요구사항4) 댓글 좋아요 API (POST)
    @PostMapping("/comment/like/{id}")
    public ResponseEntity<MessageResponseDto> createCommentLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createCommentLike(id, userDetails.getUser());
    }
}
