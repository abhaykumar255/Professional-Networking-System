package com.professionalnetworking.postsservice.clients;

import com.professionalnetworking.postsservice.dto.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "connection-service", path = "/connections")
public interface ConnectionClients {

    @GetMapping("/core/first-degree") // This will give us the first degree connections of the user
    List<PersonDTO> getMyFirstDegreeConnections();
}
