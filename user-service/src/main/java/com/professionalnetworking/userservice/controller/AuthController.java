package com.professionalnetworking.userservice.controller;

import com.professionalnetworking.userservice.dto.LoginRequestDTO;
import com.professionalnetworking.userservice.dto.ResetPasswordRequestDTO;
import com.professionalnetworking.userservice.dto.SignUpRequestDTO;
import com.professionalnetworking.userservice.dto.UserDTO;
import com.professionalnetworking.userservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {

        UserDTO userDTO = authService.signUp(signUpRequestDTO);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> signUp(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {

        String token = authService.login(loginRequestDTO);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequestDTO) {

        authService.resetPassword(resetPasswordRequestDTO);

        return ResponseEntity.ok("Password reset successfully");
    }

}
