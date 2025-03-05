package com.example.backend.service;

import com.example.backend.entity.Basket;
import com.example.backend.entity.Product;
import com.example.backend.repository.BasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @InjectMocks
    private BasketService basketService;

    private Basket testBasket;

    @BeforeEach
    public void setUp() {
        testBasket = new Basket();
        testBasket.setId(1L);
        testBasket.setProducts(Collections.singletonList(new Product(1L, "Product Test", "Description", "url", 10.0f)));
    }

    @Test
    public void testFindAll_ReturnsBasketList() {
        List<Basket> baskets = Arrays.asList(testBasket);
        when(basketRepository.findAll()).thenReturn(baskets);

        List<Basket> result = basketService.findAll();

        assertEquals(1, result.size());
        assertEquals(testBasket, result.get(0));
        verify(basketRepository).findAll();
    }

    @Test
    public void testFindById_ExistingBasket_ReturnsBasket() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(testBasket));

        Optional<Basket> result = basketService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(testBasket, result.get());
        verify(basketRepository).findById(1L);
    }

    @Test
    public void testFindById_NonExistingBasket_ReturnsEmpty() {
        when(basketRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Basket> result = basketService.findById(99L);

        assertFalse(result.isPresent());
        verify(basketRepository).findById(99L);
    }

    @Test
    public void testSave_ValidBasket_ReturnsSavedBasket() {
        when(basketRepository.save(any(Basket.class))).thenReturn(testBasket);

        Basket savedBasket = basketService.save(testBasket);

        assertNotNull(savedBasket);
        assertEquals(1L, savedBasket.getId());
        verify(basketRepository).save(testBasket);
    }

    @Test
    public void testSave_EmptyBasket_ReturnsSavedBasket() {
        Basket emptyBasket = new Basket();
        emptyBasket.setProducts(Collections.emptyList());

        when(basketRepository.save(any(Basket.class))).thenReturn(emptyBasket);

        Basket savedBasket = basketService.save(emptyBasket);

        assertNotNull(savedBasket);
        assertTrue(savedBasket.getProducts().isEmpty());
        verify(basketRepository).save(emptyBasket);
    }

    @Test
    public void testDelete_ExistingBasket_ReturnsTrue() {
        when(basketRepository.findById(1L)).thenReturn(Optional.of(testBasket));
        doNothing().when(basketRepository).delete(testBasket);

        boolean result = basketService.delete(1L);

        assertTrue(result);
        verify(basketRepository).findById(1L);
        verify(basketRepository).delete(testBasket);
    }

    @Test
    public void testDelete_NonExistingBasket_ReturnsFalse() {
        when(basketRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = basketService.delete(99L);

        assertFalse(result);
        verify(basketRepository).findById(99L);
        verify(basketRepository, never()).delete(any());
    }
}
