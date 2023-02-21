package com.example.blogservice.repository;


import com.example.blogservice.entity.Comment;
import com.example.blogservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByIdAndUserId(Long id, Long UserId);
}
