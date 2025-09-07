package com.professionalnetworking.postsservice.services;

import com.professionalnetworking.postsservice.auth.UserContextHolder;
import com.professionalnetworking.postsservice.clients.ConnectionClients;
import com.professionalnetworking.postsservice.dto.PersonDTO;
import com.professionalnetworking.postsservice.dto.PostDTO;
import com.professionalnetworking.postsservice.entity.Post;
import com.professionalnetworking.postsservice.dto.PostCreateRequestDTO;
import com.professionalnetworking.postsservice.event.PostCreatedEvent;
import com.professionalnetworking.postsservice.exception.custom_exception.ResourceNotFoundException;
import com.professionalnetworking.postsservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionClients connectionClients;

    private final KafkaTemplate<Long, PostCreatedEvent > kafkaTemplate;


    public PostDTO createPost(PostCreateRequestDTO postCreateDTO) {

        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postCreateDTO, Post.class);
        post.setUserId(userId);

        Post savedPost = postRepository.save(post);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .creatorId(userId)
                .postId(savedPost.getId())
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);

        return modelMapper.map(savedPost, PostDTO.class);
    }

    public PostDTO getPostById(Long postId) {
        log.info("Retrieving post with id: {}", postId);

        Long userId = UserContextHolder.getCurrentUserId();
        log.info("User id from header: {}", userId);

        //List<PersonDTO> firstDegreeConnections = connectionClients.getMyFirstDegreeConnections();

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
