package com.professionalnetworking.postsservice.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
public class PostCreatedEvent {

    Long creatorId;
    String content;
    Long postId;
}
