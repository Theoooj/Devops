package com.example.backend.controller;

import com.example.backend.dto.request.AuthRequestDTO;
import com.example.backend.entity.User;
import com.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequestDTO request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthRequestDTO request) {
        User user = authService.register(request);
        return ResponseEntity.ok(user);
    }
}
