package com.professionalnetworking.notificationservice.service;

import com.professionalnetworking.notificationservice.entity.Notification;
import com.professionalnetworking.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendNotification {

    private final NotificationRepository notificationRepository;

    public void send(Long userId, String message) {
        log.info("Sending notification to: {}", userId);
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .build();

        notificationRepository.save(notification);
    }
}
