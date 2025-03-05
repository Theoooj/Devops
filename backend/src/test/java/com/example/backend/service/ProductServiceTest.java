package com.example.backend.service;

import com.example.backend.entity.Product;
import com.example.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

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
    public void testFindAll_ReturnsListOfProducts() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.findAll();

        // Assert
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository).findAll();
    }

    @Test
    public void testFindById_ExistingProduct_ReturnsProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Optional<Product> foundProduct = productService.findById(1L);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals(testProduct, foundProduct.get());
        verify(productRepository).findById(1L);
    }

    @Test
    public void testFindById_NonExistingProduct_ReturnsEmptyOptional() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Product> foundProduct = productService.findById(99L);

        // Assert
        assertTrue(foundProduct.isEmpty());
        verify(productRepository).findById(99L);
    }

    @Test
    public void testSave_NewProduct_ReturnsSavedProduct() {
        // Arrange
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(29);

        when(productRepository.save(newProduct)).thenReturn(newProduct);

        // Act
        Product savedProduct = productService.save(newProduct);

        // Assert
        assertEquals(newProduct, savedProduct);
        verify(productRepository).save(newProduct);
    }

    @Test
    public void testUpdateProduct_ExistingProduct_UpdatesAndReturnsProduct() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Original Product");
        existingProduct.setPrice(19);

        Product updateDetails = new Product();
        updateDetails.setName("Updated Product");
        updateDetails.setPrice(29);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        Optional<Product> updatedProduct = productService.updateProduct(1L, updateDetails);

        // Assert
        assertTrue(updatedProduct.isPresent());
        assertEquals("Updated Product", updatedProduct.get().getName());
        assertEquals((29.0), updatedProduct.get().getPrice());
        verify(productRepository).findById(1L);
        verify(productRepository).save(existingProduct);
    }

    @Test
    public void testUpdateProduct_NonExistingProduct_ReturnsEmptyOptional() {
        // Arrange
        Product updateDetails = new Product();
        updateDetails.setName("Update Attempt");

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Product> updatedProduct = productService.updateProduct(99L, updateDetails);

        // Assert
        assertTrue(updatedProduct.isEmpty());
        verify(productRepository).findById(99L);
        verify(productRepository, never()).save(any());
    }

    @Test
    public void testDelete_ExistingProduct_DeletesAndReturnsTrue() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(testProduct);

        // Act
        boolean result = productService.delete(1L);

        // Assert
        assertTrue(result);
        verify(productRepository).findById(1L);
        verify(productRepository).delete(testProduct);
    }

    @Test
    public void testDelete_NonExistingProduct_ReturnsFalse() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        boolean result = productService.delete(99L);

        // Assert
        assertFalse(result);
        verify(productRepository).findById(99L);
        verify(productRepository, never()).delete(any());
    }
}