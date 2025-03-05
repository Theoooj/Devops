package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("rawPassword");
    }

    @Test
    public void testGetAllUsers_ReturnsUserList() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository).findAll();
    }

    @Test
    public void testGetUserById_ExistingUser_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findById(1L);
    }

    @Test
    public void testGetUserById_NonExistingUser_ReturnsEmpty() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(99L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(99L);
    }

    @Test
    public void testGetUserByEmail_ExistingUser_ReturnsUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    public void testCreateUser_NewUser_SavesUser() {
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("rawPassword");

        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User savedUser = userService.createUser(newUser);

        assertNotNull(savedUser);
        verify(userRepository).existsByEmail(newUser.getEmail());
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(newUser);

        assertEquals("encodedPassword", newUser.getPassword());
    }

    @Test
    public void testCreateUser_ExistingEmail_ThrowsException() {
        User newUser = new User();
        newUser.setEmail("existing@example.com");

        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUser);
        });
    }

    @Test
    public void testUpdateUser_ExistingUser_UpdatesUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPassword");

        User updateDetails = new User();
        updateDetails.setEmail("new@example.com");
        updateDetails.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updateDetails.getPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        Optional<User> updatedUser = userService.updateUser(1L, updateDetails);

        assertTrue(updatedUser.isPresent());
        assertEquals("new@example.com", updatedUser.get().getEmail());
        verify(passwordEncoder).encode(updateDetails.getPassword());
        verify(userRepository).save(existingUser);
    }

    @Test
    public void testUpdateUser_NonExistingUser_ReturnsEmpty() {
        User updateDetails = new User();
        updateDetails.setEmail("new@example.com");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> updatedUser = userService.updateUser(99L, updateDetails);

        assertTrue(updatedUser.isEmpty());
        verify(userRepository).findById(99L);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testDeleteUser_ExistingUser_DeletesUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    public void testDeleteUser_NonExistingUser_ReturnsFalse() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(99L);

        assertFalse(result);
        verify(userRepository).findById(99L);
        verify(userRepository, never()).delete(any());
    }
}