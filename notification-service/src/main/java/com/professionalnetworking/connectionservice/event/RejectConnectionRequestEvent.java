package com.professionalnetworking.connectionservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RejectConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
