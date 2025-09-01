package com.professionalnetworking.postsservice.services;

import com.professionalnetworking.postsservice.entity.PostLikes;
import com.professionalnetworking.postsservice.exception.custom_exception.BadRequestException;
import com.professionalnetworking.postsservice.exception.custom_exception.ResourceNotFoundException;
import com.professionalnetworking.postsservice.repository.PostLikeRepository;
import com.professionalnetworking.postsservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public void likePost(Long postId, Long userId) {
        log.info("Attempting to like post with id: {}", postId);
        boolean exits = postRepository.existsById(postId);
        if (!exits) {
            log.error("Post not found with id : {}", postId);
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        boolean alreadyExists = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyExists) {
            log.error("Can not like post twice");
            throw new BadRequestException("Can not like post twice ");
        }

        PostLikes postLike = new PostLikes();
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);
        log.info("Post with id: {} liked successfully", postId);
    }

    public void unLikePost(Long postId, long userId) {
        log.info("Attempting to unLike post with id: {}", postId);
        boolean exits = postRepository.existsById(postId);
        if (!exits) {
            log.error("Post not found with id: {}", postId);
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (!alreadyLiked) {
            log.error("Can not Unlike the post as you have not liked it yet");
            throw new BadRequestException("Can not Unlike the post as you have not liked it yet");
        }

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);

        log.info("Post with id: {} Unliked successfully", postId);
    }
}
