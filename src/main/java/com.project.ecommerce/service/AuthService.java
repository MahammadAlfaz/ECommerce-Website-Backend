package com.project.ecommerce.service;

import com.project.ecommerce.dto.AuthenticationResult;
import com.project.ecommerce.dto.UserResponse;
import com.project.ecommerce.security.jwt.request.LoginRequest;
import com.project.ecommerce.security.jwt.request.SignupRequest;
import com.project.ecommerce.security.jwt.response.MessageResponse;
import com.project.ecommerce.security.jwt.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {



        AuthenticationResult login(LoginRequest loginRequest);

        ResponseEntity<MessageResponse> register(SignupRequest signUpRequest);

        UserInfoResponse getCurrentUserDetails(Authentication authentication);

        ResponseCookie logoutUser();

        UserResponse getAllSellers(Pageable pageable);
    }

