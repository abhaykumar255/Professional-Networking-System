package com.professionalnetworking.connectionservice.service;

import com.professionalnetworking.connectionservice.auth.UserContextHolder;
import com.professionalnetworking.connectionservice.client.UserServiceClient;
import com.professionalnetworking.connectionservice.dto.UserDTO;
import com.professionalnetworking.connectionservice.entity.Person;
import com.professionalnetworking.connectionservice.event.AcceptConnectionRequestEvent;
import com.professionalnetworking.connectionservice.event.RejectConnectionRequestEvent;
import com.professionalnetworking.connectionservice.event.SendConnectionRequestEvent;
import com.professionalnetworking.connectionservice.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserServiceClient userServiceClient;

    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptKafkaTemplate;
    private final KafkaTemplate<Long, RejectConnectionRequestEvent> rejectKafkaTemplate;


    public List<Person> getMyFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Retrieving first degree connections for user with id: {}", userId );
        // Ensure person exists in the database
        ensurePersonExists(userId);
        return connectionRepository.getFirstDegreeConnections(userId);
    }

    public List<Person> getAllPeople() {
        return connectionRepository.findAll();
    }

    /*
    * 1. Checking if the user is already connected
    * 2. Checking if the user has already sent a request
    * 3. Checking if the user has already received a request
    * 4. Send the request
     */
    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Sending connection request from user with id: {} to user with id: {}", senderId, receiverId);

        // Ensure both persons exist in the database with proper names
        ensurePersonExists(senderId);
        ensurePersonExists(receiverId);

        validateConnectionRequest(senderId, receiverId);

        connectionRepository.addConnectionRequest(senderId, receiverId);
        log.info("Connection request sent successfully from user with id: {} to user with id: {}", senderId, receiverId);

        sendKafkaTemplate.send("connection-request-topic", SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build());

        return true;
    }

    /*
    * 1. Checking if the user has already sent a request
    * 2. Accept the request
    * 3. Delete the REQUESTED_TO relationship
    * 4. Create the CONNECTED_TO relationship
     */
    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("Accepting connection request from user with id: {} to user with id: {}", senderId, receiverId);

        // Ensure both persons exist in the database with proper names
        ensurePersonExists(senderId);
        ensurePersonExists(receiverId);

        if (!connectionRepository.connectionRequestExists(senderId, receiverId)) {
            log.error("No connection request found");
            throw new RuntimeException("No connection request found");
        }
        connectionRepository.acceptConnectionRequest(senderId, receiverId);
        log.info("Connection request accepted successfully from user with id: {} to user with id: {}", senderId, receiverId);

        acceptKafkaTemplate.send("connection-acceptance-topic", AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build());

        return true;
    }

    /*
    * 1. Checking if the user has already sent a request
    * 2. Reject the request
    * 3. Delete the REQUESTED_TO relationship
     */
    public Boolean rejectConnectionRequest(Long userId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("Rejecting connection request from user with id: {} to user with id: {}", userId, receiverId);

        // Ensure both persons exist in the database with proper names
        ensurePersonExists(userId);
        ensurePersonExists(receiverId);

        if (!connectionRepository.connectionRequestExists(userId, receiverId)) {
            log.error("No connection request found, can not reject");
            throw new RuntimeException("No connection request found, can not reject");
        }
        connectionRepository.rejectConnectionRequest(userId, receiverId);
        log.info("Connection request rejected successfully from user with id: {} to user with id: {}", userId, receiverId);

        rejectKafkaTemplate.send("connection-rejection-topic", RejectConnectionRequestEvent.builder()
                .senderId(userId)
                .receiverId(receiverId)
                .build());

        return true;
    }

    private void ensurePersonExists(Long userId) {
        if (!connectionRepository.findByUserId(userId).isPresent()) {
            try {
                // Fetch user details from user service
                UserDTO userDetails = userServiceClient.getUserById(userId);
                connectionRepository.createOrUpdatePerson(userId, userDetails.getName());
                log.info("Created person with userId: {} and name: {}", userId, userDetails.getName());
            } catch (Exception e) {
                log.warn("Could not fetch user details for userId: {}, creating with default name. Error: {}", userId, e.getMessage());
                connectionRepository.createOrUpdatePersonWithDefault(userId);
            }
        }
    }


    private void validateConnectionRequest(Long senderId, Long receiverId) {
        validateNotSelfRequest(senderId, receiverId);
        validateNotAlreadyConnected(senderId, receiverId);
        validateNoExistingRequest(senderId, receiverId);
    }

    private void validateNotSelfRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            log.error("Cannot send connection request to yourself");
            throw new RuntimeException("Cannot send connection request to yourself");
        }
    }

    private void validateNotAlreadyConnected(Long senderId, Long receiverId) {
        if (connectionRepository.alreadyConnected(senderId, receiverId)) {
            log.error("You are already connected to this person");
            throw new RuntimeException("You are already connected to this person");
        }
    }

    private void validateNoExistingRequest(Long senderId, Long receiverId) {
        if (connectionRepository.connectionRequestExists(senderId, receiverId)) {
            log.error("You have already sent a connection request to this person");
            throw new RuntimeException("You have already sent a connection request to this person");
        }
    }
}
