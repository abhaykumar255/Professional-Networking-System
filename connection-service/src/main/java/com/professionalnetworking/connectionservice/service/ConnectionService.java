package com.professionalnetworking.connectionservice.service;

import com.professionalnetworking.connectionservice.auth.UserContextHolder;
import com.professionalnetworking.connectionservice.entity.Person;
import com.professionalnetworking.connectionservice.repository.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    private final ConnectionRepository connectionRepository;

    public List<Person> getMyFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Retrieving first degree connections for user with id: {}", userId );
        return connectionRepository.getFirstDegreeConnections(userId);
    }

    public List<Person> getAllPeople() {
        return connectionRepository.findAll();
    }
}
