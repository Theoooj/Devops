package com.example.backend.controller;

import com.example.backend.entity.Basket;
import com.example.backend.service.BasketService;
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

@WebMvcTest(BasketController.class)
@Import(BasketControllerTest.SecurityConfig.class)
public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BasketService basketService;

    @Autowired
    private ObjectMapper objectMapper;

    private Basket testBasket;

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
        public BasketService basketService() {
            return mock(BasketService.class);
        }
    }

    @BeforeEach
    public void setUp() {
        testBasket = new Basket();
        testBasket.setId(1L);
        testBasket.setProducts(List.of());
    }

    @Test
    public void testGetAllBaskets_ReturnsBasketList() throws Exception {
        List<Basket> baskets = Arrays.asList(testBasket);
        when(basketService.findAll()).thenReturn(baskets);

        mockMvc.perform(get("/baskets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(basketService).findAll();
    }

    @Test
    public void testGetBasketById_ExistingBasket_ReturnsBasket() throws Exception {
        when(basketService.findById(1L)).thenReturn(Optional.of(testBasket));

        mockMvc.perform(get("/baskets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(basketService).findById(1L);
    }

    @Test
    public void testGetBasketById_NonExistingBasket_ReturnsNotFound() throws Exception {
        when(basketService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/baskets/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateBasket_ValidBasket_ReturnsCreatedBasket() throws Exception {
        when(basketService.save(any(Basket.class))).thenReturn(testBasket);

        mockMvc.perform(post("/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBasket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(basketService).save(any(Basket.class));
    }

    @Test
    public void testCreateBasket_InvalidBasket_ReturnsBadRequest() throws Exception {
        Basket invalidBasket = new Basket();

        mockMvc.perform(post("/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBasket)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateBasket_ExistingBasket_ReturnsUpdatedBasket() throws Exception {
        when(basketService.updateBasket(eq(1L), any(Basket.class))).thenReturn(Optional.of(testBasket));

        mockMvc.perform(put("/baskets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBasket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(basketService).updateBasket(eq(1L), any(Basket.class));
    }

    @Test
    public void testDeleteBasket_ExistingBasket_ReturnsNoContent() throws Exception {
        when(basketService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/baskets/1"))
                .andExpect(status().isNoContent());

        verify(basketService).delete(1L);
    }
}