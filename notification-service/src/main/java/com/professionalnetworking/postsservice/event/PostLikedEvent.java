package com.professionalnetworking.postsservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLikedEvent {

    Long postId;
    Long creatorId;
    Long likedByUserId;
}
