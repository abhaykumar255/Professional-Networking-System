package com.professionalnetworking.connectionservice.client;

import com.professionalnetworking.connectionservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/users")
public interface UserServiceClient {

    @GetMapping("/auth/{userId}")
    UserDTO getUserById(@PathVariable Long userId);
}
