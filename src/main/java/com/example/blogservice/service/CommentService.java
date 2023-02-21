package com.example.blogservice.service;


import com.example.blogservice.dto.CommentRequestDto;
import com.example.blogservice.dto.CommentResponseDto;
import com.example.blogservice.dto.MessageResponseDto;
import com.example.blogservice.entity.*;
import com.example.blogservice.repository.BlogRepository;
import com.example.blogservice.repository.CommentLikeRepository;
import com.example.blogservice.repository.CommentRepository;
import com.example.blogservice.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static com.example.blogservice.util.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class CommentService {
    // 1) CommentRepo 의존성 주입.
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 요구사항 1) 댓글 작성
    @Transactional
    public ResponseEntity<CommentResponseDto> createComment(Long id, CommentRequestDto commentRequestDto, User user) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BLOG)
        );
        Comment comment = commentRepository.save(Comment.builder()
                .commentRequestDto(commentRequestDto)
                .user(user)
                .blog(blog)
                .build());
        return ResponseEntity.ok()
                .body(new CommentResponseDto(comment));
    }

    // 요구사항 2) 댓글 수정
    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(Long id, CommentRequestDto commentRequestDto, User user) {

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_COMMENT)
        );
        // 3) Admin 권한이 있는 친구는 전부 수정, 아닌경우 일부수정.
        UserRoleEnum userRoleEnum = user.getRole();

        Optional<Comment> comment;

        // 3-1) Admin 권환인 경우.
        if (userRoleEnum == UserRoleEnum.ADMIN) {
            comment = commentRepository.findById(id);
            if (comment.isEmpty()) { // 일치하는 댓글이 없다면
                throw new CustomException(NOT_FOUND_COMMENT);
            }

        } else { // 3-2) User 권한인 경우.
            comment = commentRepository.findByIdAndUserId(id, user.getId());
            if (comment.isEmpty()) { // 일치하는 댓글이 없다면
                throw new CustomException(NOT_FOUND_COMMENT);
            }
        }
        // 4) Comment Update
        comment.get().update(commentRequestDto, user);

        // 5) ResponseEntity에 Body 부분에 만든 객체 전달.
        return ResponseEntity.ok()
                .body(new CommentResponseDto(comment.get(),commentLikeRepository.countCommentLikesByCommentId(comment.get().getId())));
    }

    // 요구사항 3) 댓글 삭제
    @Transactional
    public ResponseEntity<MessageResponseDto> deleteComment(Long id, User user) {
        // 게시글의 DB 저장 유무 확인
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BLOG)
        );

        // 사용자 권한 가져와서 ADMIN 이면 무조건 수정 가능, USER 면 본인이 작성한 댓글일 때만 수정 가능
        UserRoleEnum userRoleEnum = user.getRole();

        Comment comment;

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 댓글 id와 일치하는 DB 조회
            comment = commentRepository.findById(id).orElseThrow(
                    () -> new CustomException(NOT_FOUND_COMMENT)
            );
        } else {
            // 입력 받은 댓글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
            comment = commentRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }

        // 4) Comment Delete
        commentRepository.deleteById(id);

        // 5) ResponseEntity에 Body 부분에 만든 객체 전달.
        return ResponseEntity.ok()
                .body(MessageResponseDto.builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("댓글 삭제 성공.")
                        .build()
                );
    }
    // 요구사항 4) 댓글 좋아요
    @Transactional
    public ResponseEntity<MessageResponseDto> createCommentLike(Long id, User user) {

        // 입력 받은 댓글 id와 일치하는 DB 조회
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_COMMENT)
        );
        Optional<CommentLike> commentLike = commentLikeRepository.findByCommentIdAndUserId(id, user.getId());
        if (commentLike.isEmpty()) {
            commentLikeRepository.saveAndFlush(new CommentLike(comment, user));
            return ResponseEntity.ok()
                    .body(MessageResponseDto.builder()
                            .statusCode(HttpStatus.OK.value())
                            .msg("댓글 좋아요 선택")
                            .build()
                    );
        } else {
            commentLikeRepository.deleteByCommentIdAndUserId(id, user.getId());
            return ResponseEntity.ok()
                    .body(MessageResponseDto.builder()
                            .statusCode((HttpStatus.OK.value()))
                            .msg("댓글 좋아요 취소")
                            .build()
                    );

        }

    }
}
