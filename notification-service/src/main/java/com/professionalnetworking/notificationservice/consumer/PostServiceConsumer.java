package com.professionalnetworking.notificationservice.consumer;

import com.professionalnetworking.notificationservice.clients.ConnectionClients;
import com.professionalnetworking.notificationservice.dto.PersonDTO;
import com.professionalnetworking.notificationservice.entity.Notification;
import com.professionalnetworking.notificationservice.repository.NotificationRepository;
import com.professionalnetworking.notificationservice.service.SendNotification;
import com.professionalnetworking.postsservice.event.PostCreatedEvent;
import com.professionalnetworking.postsservice.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionClients connectionClients;
    private final NotificationRepository notificationRepository;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic", groupId = "post-created-group")
    public void consumePostCreatedEvent(PostCreatedEvent event) {
        log.info("Received post created event: {}", event);

        // Send notification to all first degree connections
        List<PersonDTO> connections = connectionClients.getMyFirstDegreeConnections(event.getCreatorId());

        for (PersonDTO connection : connections ) {
            sendNotification.send(connection.getUserId(), "Your Connection "+event.getCreatorId()+" has created a post");
        }
    }

    @KafkaListener(topics = "post-liked-topic", groupId = "post-liked-group")
    public void consumePostLikedEvent(PostLikedEvent event) {
        log.info("Received post liked event: {}", event);
        String message = String.format("You post, %d, has been liked by %d", event.getPostId(), event.getLikedByUserId());

        sendNotification.send(event.getCreatorId(), message);
    }

}
