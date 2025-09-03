package com.professionalnetworking.connectionservice.controller;

import com.professionalnetworking.connectionservice.entity.Person;
import com.professionalnetworking.connectionservice.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getMyFirstDegreeConnections() {
        //String userId = httpServletRequest.getHeader("X-USER-ID");
        return ResponseEntity.ok(connectionService.getMyFirstDegreeConnections());
    }
}
