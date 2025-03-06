package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @TestConfiguration
    static class SecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                    .csrf(csrf -> csrf.disable());
            return http.build();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring().anyRequest();
        }
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @BeforeEach
    public void setUp() {
        reset(userService);

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    public void testGetAllUsers_ReturnsUserList() throws Exception {
        List<User> users = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("test@example.com")));

        verify(userService).getAllUsers();
    }

    @Test
    public void testGetUserById_ExistingUser_ReturnsUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService).getUserById(1L);
    }

    @Test
    public void testGetUserById_NonExistingUser_ReturnsNotFound() throws Exception {
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(99L);
    }

    @Test
    public void testGetUserByEmail_ExistingUser_ReturnsUser() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/users/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("test@example.com")));

        verify(userService).getUserByEmail("test@example.com");
    }

    @Test
    public void testGetUserByEmail_NonExistingUser_ReturnsNotFound() throws Exception {
        when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/email/nonexistent@example.com"))
                .andExpect(status().isNotFound());

        verify(userService).getUserByEmail("nonexistent@example.com");
    }

    @Test
    public void testCreateUser_ValidUser_ReturnsCreatedUser() throws Exception {
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("newpassword");
        newUser.setCreatedAt(LocalDateTime.now());

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("new@example.com")));

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void testCreateUser_InvalidUser_ReturnsBadRequest() throws Exception {
        User invalidUser = new User();
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser_ExistingUser_ReturnsUpdatedUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(updatedUser));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("updated@example.com")));

        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    public void testUpdateUser_NonExistingUser_ReturnsNotFound() throws Exception {
        User updateUser = new User();
        updateUser.setEmail("update@example.com");

        when(userService.updateUser(eq(99L), any(User.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isNotFound());

        verify(userService).updateUser(eq(99L), any(User.class));
    }

    @Test
    public void testDeleteUser_ExistingUser_ReturnsNoContent() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    public void testDeleteUser_NonExistingUser_ReturnsNotFound() throws Exception {
        when(userService.deleteUser(99L)).thenReturn(false);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(99L);
    }
}