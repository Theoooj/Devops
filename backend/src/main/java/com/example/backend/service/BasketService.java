package com.example.backend.service;


import com.example.backend.entity.Basket;
import com.example.backend.repository.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasketService {
    private final BasketRepository basketRepository;

    @Autowired
    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public List<Basket> findAll() {
        return basketRepository.findAll();
    }

    public Optional<Basket> findById(Long id) {
        return basketRepository.findById(id);
    }

    public Basket save(Basket basket) {
        return basketRepository.save(basket);
    }

    public Optional<Basket> updateBasket(Long id, Basket basketDetails) {
        return basketRepository.findById(id).map(basket -> {
            basket.setProducts(basketDetails.getProducts());
            return basketRepository.save(basket);
        });
    }

    public boolean delete(Long id) {
        return basketRepository.findById(id).map(basket -> {
            basketRepository.delete(basket);
            return true;
        }).orElse(false);
    }
}

