package com.example.blogservice.controller;


import com.example.blogservice.dto.BlogRequestDto;
import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.dto.BaseResponseDto;
import com.example.blogservice.security.UserDetailsImpl;
import com.example.blogservice.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BlogController {

    // 0) DI
    private final BlogService blogService;


    // 요구사항1) 전체 게시글 목록 조회 API (GET)
    @GetMapping("/blogs")
    public ResponseEntity<List<BlogResponseDto>> getBlogs() {
        return blogService.getBlogs();
    }

    // 요구사항2) 게시글 작성 API (POST)
    @PostMapping("/blogs")
    public ResponseEntity<BlogResponseDto> createBlog(
            @RequestBody BlogRequestDto blogrequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blogService.createBlog(blogrequestDto, userDetails.getUser());
    }

    // 요구사항3) 선택한 게시글 조회 API (GET)
    @GetMapping("/blogs/{id}")
    public ResponseEntity<BlogResponseDto> getBlogs(@PathVariable Long id) {
        return blogService.getBlogs(id);
    }


    // 요구사항4) 선택한 게시글 수정 API (PUT)
    @PutMapping("/blogs/{id}")
    public ResponseEntity<BlogResponseDto> updateBlog(
            @PathVariable Long id,
            @RequestBody BlogRequestDto blogRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blogService.updateBlog(id, blogRequestDto, userDetails.getUser());
    }

    // 요구사항5) 선택한 게시글 삭제 API (DEL)
    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<BaseResponseDto> deleteBlog(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blogService.deleteBlog(id, userDetails.getUser());
    }

    // 요구사항6) 선택한 게시글 좋아요 표시 (POST)
    @PostMapping("/blogs/like/{id}")
    public ResponseEntity<BaseResponseDto> createBlogLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blogService.createBlogLike(id, userDetails.getUser());
    }
}
