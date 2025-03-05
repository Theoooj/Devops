package com.example.backend.controller;

import com.example.backend.entity.Product;
import com.example.backend.service.ProductService;
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

@WebMvcTest(ProductController.class)
@Import(ProductControllerTest.SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

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
        public ProductService productService() {
            return mock(ProductService.class);
        }
    }

    @BeforeEach
    public void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(19);
        testProduct.setSrcUrl("http://example.com/image.jpg");
    }

    @Test
    public void testGetAllProducts_ReturnsProductList() throws Exception {
        List<Product> products = Arrays.asList(testProduct);
        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Product")));
    }

    @Test
    public void testGetProductById_ExistingProduct_ReturnsProduct() throws Exception {
        when(productService.findById(1L)).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(19.0)));
    }

    @Test
    public void testGetProductById_NonExistingProduct_ReturnsNotFound() throws Exception {
        when(productService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct_ValidProduct_ReturnsCreatedProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(29);

        when(productService.save(any(Product.class))).thenReturn(newProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.price", is(29.0)));

        verify(productService).save(any(Product.class));
    }


    @Test
    public void testCreateProduct_InvalidData_ReturnsBadRequest() throws Exception {
        Product invalidProduct = new Product();
        invalidProduct.setPrice(19); // Nom manquant

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateProduct_NegativePrice_ReturnsBadRequest() throws Exception {
        Product invalidProduct = new Product();
        invalidProduct.setName("Invalid Product");
        invalidProduct.setPrice(-10);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateProduct_ExistingProduct_ReturnsUpdatedProduct() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(39);

        when(productService.updateProduct(eq(1L), any(Product.class)))
                .thenReturn(Optional.of(updatedProduct));

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(39.0)));

        verify(productService).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    public void testUpdateProduct_NegativePrice_ReturnsBadRequest() throws Exception {
        Product invalidUpdate = new Product();
        invalidUpdate.setName("Updated Product");
        invalidUpdate.setPrice(-5);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteProduct_ExistingProduct_ReturnsNoContent() throws Exception {
        when(productService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).delete(1L);
    }

    @Test
    public void testDeleteProduct_NonExistingProduct_ReturnsNotFound() throws Exception {
        when(productService.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/products/99"))
                .andExpect(status().isNotFound());

        verify(productService).delete(99L);
    }
}
