package com.example.blogservice.service;


import com.example.blogservice.dto.*;
import com.example.blogservice.entity.*;
import com.example.blogservice.repository.BlogLikeRepository;
import com.example.blogservice.repository.BlogRepository;
import com.example.blogservice.repository.CommentLikeRepository;
import com.example.blogservice.util.CustomException;
import com.example.blogservice.util.ErrorCode;
import com.example.blogservice.util.SuccessCode;
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
        List<BlogResponseDto> blogResponseList = new ArrayList<>();

        for (Blog blog : blogList) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : blog.getCommentList()) {
                commentList.add(CommentResponseDto.from(comment, commentLikeRepository.countCommentLikesByCommentId(comment.getId())));
            }
            blogResponseList.add(BlogResponseDto.from(blog, commentList, blogLikeRepository.countBlogLikesByBlogId(blog.getId())));
        }
        return ResponseEntity.ok()
                .body(blogResponseList);
    }

    // 요구사항 2) 게시글 작성
    @Transactional
    public ResponseEntity<BlogResponseDto> createBlog(BlogRequestDto blogrequestDto, User user) {

        Blog blog = blogRepository.saveAndFlush(Blog.of(blogrequestDto, user));

        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog));
    }


    // 요구사항 3)  선택한 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<BlogResponseDto> getBlogs(Long id) {

        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isEmpty()) {
            throw new CustomException(NOT_FOUND_BLOG);
        }

        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : blog.get().getCommentList()) {
            commentList.add(CommentResponseDto.from
                    (comment, commentLikeRepository.countCommentLikesByCommentId(comment.getId())));
        }

        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog.get(), commentList, blogLikeRepository.countBlogLikesByBlogId(blog.get().getId())));
    }

    // 요구사항4. 선택한 게시글 수정
    @Transactional
    public ResponseEntity<BlogResponseDto> updateBlog(Long id, BlogRequestDto blogRequestDto, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        Optional<Blog> blog;
        List<CommentResponseDto> commentList = new ArrayList<>();

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            blog = blogRepository.findById(id);
            if(blog.isEmpty())
            {
                throw new CustomException(NOT_FOUND_BLOG);
            }
        } else {
            blog = blogRepository.findByIdAndUserId(id, user.getId());
            if(blog.isEmpty())
            {
                throw  new CustomException(AUTHORIZATION);
            }
        }

        blog.get().update(blogRequestDto, user);

        for (Comment comment : blog.get().getCommentList()) {
            commentList.add(CommentResponseDto.from(comment, commentLikeRepository.countCommentLikesByCommentId(comment.getId())));
        }

        return ResponseEntity.ok()
                .body(BlogResponseDto.from(blog.get(), commentList, blogLikeRepository.countBlogLikesByBlogId(blog.get().getId())));
    }

    // 요구사항5. 선택한 게시글 삭제
    @Transactional
    public ResponseEntity<BaseResponseDto> deleteBlog(Long id, User user) {

        UserRoleEnum userRoleEnum = user.getRole();
        Optional<Blog> blog;

        if (userRoleEnum == UserRoleEnum.ADMIN) {
            blog = blogRepository.findById(id);
            if(blog.isEmpty())
            {
                throw new CustomException(NOT_FOUND_BLOG);
            }
        } else {
            blog = blogRepository.findByIdAndUserId(id, user.getId());
            if(blog.isEmpty())
            {
                throw  new CustomException(AUTHORIZATION);
            }
        }

        blogRepository.deleteById(id);

        return ResponseEntity.ok()
                .body(BaseResponseDto.of(SuccessCode.BLOG_DELETE_SUCCESS));

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
                    .body(BaseResponseDto.of(SuccessCode.LIKE_SUCCESS));
        } else {
            blogLikeRepository.deleteByBlogIdAndUserId(id, user.getId());
            return ResponseEntity.ok()
                    .body(BaseResponseDto.of(SuccessCode.NOT_LIKE_SUCCESS));
        }

    }

}
