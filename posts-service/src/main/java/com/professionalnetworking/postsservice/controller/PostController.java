package com.professionalnetworking.postsservice.controller;

import com.professionalnetworking.postsservice.dto.PostDTO;
import com.professionalnetworking.postsservice.dto.PostCreateRequestDTO;
import com.professionalnetworking.postsservice.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostCreateRequestDTO postCreateDTO) {

        // Will be getting the user id from the JWT token
        PostDTO postDTO = postService.createPost(postCreateDTO);

        return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO postDTO = postService.getPostById(postId);

        return ResponseEntity.ok(postDTO);
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable Long userId) {
        List<PostDTO> postDTOs = postService.getPostsByUserId(userId);

        return ResponseEntity.ok(postDTOs);
    }

}
