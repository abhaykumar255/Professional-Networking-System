package com.professionalnetworking.connectionservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class RejectConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
