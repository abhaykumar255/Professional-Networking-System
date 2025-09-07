package com.professionalnetworking.notificationservice.consumer;

import com.professionalnetworking.connectionservice.event.AcceptConnectionRequestEvent;
import com.professionalnetworking.connectionservice.event.RejectConnectionRequestEvent;
import com.professionalnetworking.connectionservice.event.SendConnectionRequestEvent;
import com.professionalnetworking.notificationservice.entity.Notification;
import com.professionalnetworking.notificationservice.repository.NotificationRepository;
import com.professionalnetworking.notificationservice.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final NotificationRepository notificationRepository;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "connection-request-topic", groupId = "connection-request-group")
    public void consumeConnectionRequestEvent(SendConnectionRequestEvent event) {
        log.info("Received connection request event: {}", event);
        String message = String.format("You have received a connection request from %d", event.getSenderId());

        sendNotification.send(event.getReceiverId(), message);
    }

    @KafkaListener(topics = "connection-acceptance-topic", groupId = "connection-acceptance-group")
    public void consumeConnectionAcceptanceEvent(AcceptConnectionRequestEvent event) {
        log.info("Received connection acceptance event: {}", event);
        String message = String.format("Your connection request to %d has been accepted", event.getReceiverId());

        sendNotification.send(event.getSenderId(), message);
    }

    @KafkaListener(topics = "connection-rejection-topic", groupId = "connection-rejection-group")
    public void consumeConnectionRejectionEvent(RejectConnectionRequestEvent event) {
        log.info("Received connection rejection event: {}", event);
        String message = String.format("Your connection request to %d has been rejected", event.getReceiverId());

        sendNotification.send(event.getSenderId(), message);
    }


}
