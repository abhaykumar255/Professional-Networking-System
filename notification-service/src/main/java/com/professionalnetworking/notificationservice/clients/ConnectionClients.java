package com.professionalnetworking.notificationservice.clients;

import com.professionalnetworking.notificationservice.dto.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connection-service", path = "/connections")
public interface ConnectionClients {

    @GetMapping("/core/first-degree") // This will give us the first-degree connections of the user
    List<PersonDTO> getMyFirstDegreeConnections(@RequestHeader("X-USER-ID") Long userId);
}
