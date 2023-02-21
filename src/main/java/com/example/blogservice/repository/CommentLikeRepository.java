package com.example.blogservice.repository;

import com.example.blogservice.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    // id1 ==  commentId, id2 == UserId
    Optional<CommentLike> findByCommentIdAndUserId(Long id1, Long id2);
    void deleteByCommentIdAndUserId(Long id1, Long id2);
    Long countCommentLikesByCommentId(Long commentId);


}
