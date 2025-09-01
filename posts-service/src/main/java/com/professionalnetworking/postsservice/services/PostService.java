package com.professionalnetworking.postsservice.services;

import com.professionalnetworking.postsservice.dto.PostDTO;
import com.professionalnetworking.postsservice.entity.Post;
import com.professionalnetworking.postsservice.dto.PostCreateRequestDTO;
import com.professionalnetworking.postsservice.exception.custom_exception.ResourceNotFoundException;
import com.professionalnetworking.postsservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;


    public PostDTO createPost(PostCreateRequestDTO postCreateDTO, Long userId) {

        Post post = modelMapper.map(postCreateDTO, Post.class);
        post.setUserId(userId);

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDTO.class);
    }

    public PostDTO getPostById(Long postId) {
        log.info("Retrieving post with id: {}", postId);
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id: " + postId));

        return modelMapper.map(post, PostDTO.class);
    }

    public List<PostDTO> getPostsByUserId(Long userId) {

        List<Post> posts = postRepository.findByUserId(userId);
        return posts
                .stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .toList();
    }
}
