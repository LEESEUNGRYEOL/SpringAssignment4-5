package com.example.blogservice.repository;

import com.example.blogservice.entity.BlogLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogLikeRepository extends JpaRepository<BlogLike,Long> {

    Optional<BlogLike> findByBlogIdAndUserId(Long id, Long id1);

    void deleteByBlogIdAndUserId(Long id, Long id1);

    Long countBlogLikesByBlogId(Long blogId);
}
