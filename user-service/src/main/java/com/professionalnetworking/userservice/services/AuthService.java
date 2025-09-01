package com.professionalnetworking.userservice.services;

import com.professionalnetworking.userservice.dto.LoginRequestDTO;
import com.professionalnetworking.userservice.dto.ResetPasswordRequestDTO;
import com.professionalnetworking.userservice.dto.SignUpRequestDTO;
import com.professionalnetworking.userservice.dto.UserDTO;
import com.professionalnetworking.userservice.entity.User;
import com.professionalnetworking.userservice.exception.custom_exception.BadRequestException;
import com.professionalnetworking.userservice.exception.custom_exception.ResourceNotFoundException;
import com.professionalnetworking.userservice.repository.AuthRepository;
import com.professionalnetworking.userservice.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;


    public UserDTO signUp(SignUpRequestDTO signUpRequestDTO) {
        log.info("Attempting to sign up user with email: {}", signUpRequestDTO.getEmail());

        if (authRepository.findByEmail(signUpRequestDTO.getEmail()).isPresent()) {
            log.error("User already exists with email: {}", signUpRequestDTO.getEmail());
            throw new BadRequestException("User already exists with email: " + signUpRequestDTO.getEmail());
        }

        User user = modelMapper.map(signUpRequestDTO, User.class);
        user.setPassword(PasswordUtil.encryptPassword(user.getPassword()));

        User savedUser = authRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        log.info("Attempting to login user with email: {}", loginRequestDTO.getEmail());
        User user = authRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", loginRequestDTO.getEmail());
                    return new ResourceNotFoundException("User not found with email: " + loginRequestDTO.getEmail());
                });

        if (!PasswordUtil.checkPassword(loginRequestDTO.getPassword(), user.getPassword())) {
            log.error("Incorrect password for user: {}", loginRequestDTO.getEmail());
            throw new BadRequestException("Incorrect password");
        }
        log.info("User logged in successfully: {}", loginRequestDTO.getEmail());
        return jwtService.generateAccessToken(user);

    }

    public void resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO) {
        log.info("Attempting to reset password for user with email: {}", resetPasswordRequestDTO.getEmail());

        User user = authRepository.findByEmail(resetPasswordRequestDTO.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", resetPasswordRequestDTO.getEmail());
                    return new ResourceNotFoundException("User not found with email: " + resetPasswordRequestDTO.getEmail());
                });

        user.setPassword(PasswordUtil.encryptPassword(resetPasswordRequestDTO.getNewPassword()));
        authRepository.save(user);

        log.info("Password reset successfully for user: {}", resetPasswordRequestDTO.getEmail());
    }
}
