package com.professionalnetworking.postsservice.repository;

import com.professionalnetworking.postsservice.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikes, Long> {

    boolean existsByUserIdAndPostId(Long userId,Long postId);

    // No EntityManager with actual transaction available for current thread - cannot reliably process 'remove' call
    @Transactional
    void deleteByUserIdAndPostId(Long userId,Long postId);
}
