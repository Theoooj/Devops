package com.example.backend.service;

import com.example.backend.dto.request.AuthRequestDTO;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String authenticate(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        return jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

    public User register(AuthRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé !");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
