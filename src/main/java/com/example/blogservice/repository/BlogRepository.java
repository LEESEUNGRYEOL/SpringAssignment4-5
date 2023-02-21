package com.example.blogservice.repository;


import com.example.blogservice.entity.Blog;
import com.example.blogservice.entity.BlogLike;
import com.example.blogservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    // 요청사항 : 수정된순이 아니라, 글이 작성된 순으로 정렬바람.
    List<Blog> findAllByOrderByCreatedAtAsc();
    Optional<Blog> findByIdAndUserId(Long id, Long UserId);


}
