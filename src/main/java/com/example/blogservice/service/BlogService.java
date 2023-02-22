package com.example.blogservice.service;


import com.example.blogservice.dto.*;
import com.example.blogservice.entity.*;
import com.example.blogservice.repository.BlogLikeRepository;
import com.example.blogservice.repository.BlogRepository;
import com.example.blogservice.repository.CommentLikeRepository;
import com.example.blogservice.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.blogservice.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BlogService {

    // 0) DI
    private final BlogRepository blogRepository;
    private final BlogLikeRepository blogLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 요구사항 1)  전체 게시글 목록 조회
    @Transactional(readOnly = true)

    public ResponseEntity<List<BlogResponseDto>> getBlogs() {

        List<Blog> blogList = blogRepository.findAllByOrderByCreatedAtAsc();
        List<BlogResponseDto> allResponseDtoList = new ArrayList<>();

        for (Blog blog : blogList) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : blog.getCommentList()) {
                commentList.add(CommentResponseDto.from(comment,commentLikeRepository.countCommentLikesByCommentId(comment.getId())));
            }
            Long blogLikes = blogLikeRepository.countBlogLikesByBlogId(blog.getId());
            allResponseDtoList.add(BlogResponseDto.from(blog, commentList, blogLikes));
        }

        return ResponseEntity.ok()
                .body(allResponseDtoList);
    }

    // 요구사항 2) 게시글 작성
    @Transactional
    public ResponseEntity<BlogResponseDto> createBlog(BlogRequestDto blogrequestDto, User user) {

        Blog blog = blogRepository.saveAndFlush(Blog.of(blogrequestDto,user));

        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog));
    }


    // 요구사항 3)  선택한 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<BlogResponseDto> getBlogs(Long id) {

        // 1) id 를 사용하여 DB 조회 및 유무 판단.
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BLOG)
        );

        // 2) 가져온 blog 에 Comment 들을 CommentList 에 추가.
        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : blog.getCommentList()) {
            commentList.add(CommentResponseDto.from
                    (comment,commentLikeRepository.countCommentLikesByCommentId(comment.getId())));
        }

        // 3) ResponseEntity 에 Body 부분에 만든 객체 전달.
        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog, commentList,blogLikeRepository.countBlogLikesByBlogId(blog.getId())));
    }

    // 요구사항4. 선택한 게시글 수정
    @Transactional
    public ResponseEntity<BlogResponseDto> updateBlog(Long id, BlogRequestDto blogRequestDto, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        Blog blog;

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 게시글 id와 일치하는 DB 조회
            blog = blogRepository.findById(id).orElseThrow(
                    () -> new CustomException(NOT_FOUND_BLOG)
            );

        } else {
            // 입력 받은 게시글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
            blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }
        blog.update(blogRequestDto, user);

        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : blog.getCommentList()) {
            commentList.add(CommentResponseDto.from(comment,commentLikeRepository.countCommentLikesByCommentId(comment.getId())));      }

        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog, commentList,blogLikeRepository.countBlogLikesByBlogId(blog.getId())));
    }

    // 요구사항5. 선택한 게시글 삭제
    @Transactional
    public ResponseEntity<BaseResponseDto> deleteBlog(Long id, User user) {

        UserRoleEnum userRoleEnum = user.getRole();
        Blog blog;
        if (userRoleEnum == UserRoleEnum.ADMIN) {
            // 입력 받은 게시글 id와 일치하는 DB 조회
            blog = blogRepository.findById(id).orElseThrow(
                    () -> new CustomException(NOT_FOUND_BLOG)
            );
        } else {
            // 입력 받은 게시글 id, 토큰에서 가져온 userId와 일치하는 DB 조회
            blog = blogRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }
        blogRepository.deleteById(id);
        // 4) ResponseEntity에 Body 부분에 만든 객체 전달.
        return ResponseEntity.ok()
                .body(BaseResponseDto.builder()
                        .statusCode(HttpStatus.OK.value())
                        .msg("게시글 삭제 성공.")
                        .build()
                );

    }

    // 요구사항 6. 게시물 좋아요
    @Transactional
    public ResponseEntity<BaseResponseDto> createBlogLike(Long id, User user) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BLOG)
        );

        Optional<BlogLike> blogLike = blogLikeRepository.findByBlogIdAndUserId(id, user.getId());
        if (blogLike.isEmpty()) {
            blogLikeRepository.saveAndFlush(BlogLike.of(blog, user));
            return ResponseEntity.ok()
                    .body(BaseResponseDto.builder()
                            .statusCode(HttpStatus.OK.value())
                            .msg("게시글 좋아요 선택")
                            .build()
                    );
        } else {
            blogLikeRepository.deleteByBlogIdAndUserId(id, user.getId());
            return ResponseEntity.ok()
                    .body(BaseResponseDto.builder()
                            .statusCode((HttpStatus.OK.value()))
                            .msg("게시글 좋아요 취소")
                            .build()
                    );
        }

    }

}
