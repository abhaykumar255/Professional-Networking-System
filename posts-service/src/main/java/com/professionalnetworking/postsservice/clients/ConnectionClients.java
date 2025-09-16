package com.professionalnetworking.postsservice.clients;

import com.professionalnetworking.postsservice.dto.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "connection-service", url = "${CONNECTION_SERVICE_URL:}")
public interface ConnectionClients {

    @GetMapping("/connections/core/first-degree") // This will give us the first degree connections of the user
    List<PersonDTO> getMyFirstDegreeConnections();
}
