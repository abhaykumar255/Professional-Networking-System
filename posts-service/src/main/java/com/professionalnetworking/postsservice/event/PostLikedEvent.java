package com.professionalnetworking.postsservice.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Builder
@Data
@Slf4j
public class PostLikedEvent {

    Long postId;
    Long creatorId;
    Long likedByUserId;
}
