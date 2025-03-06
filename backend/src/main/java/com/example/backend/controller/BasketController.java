package com.example.backend.controller;

import com.example.backend.entity.Basket;
import com.example.backend.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/baskets")
public class BasketController {

    private final BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping
    public ResponseEntity<List<Basket>> getAllBaskets() {
        List<Basket> baskets = basketService.findAll();
        return ResponseEntity.ok(baskets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable Long id) {
        Optional<Basket> basket = basketService.findById(id);
        return basket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createBasket(@Validated @RequestBody Basket basket) {
        if (basket.getProducts() == null) {
            return ResponseEntity.badRequest().body("Le panier ne peut pas être null");
        }
        return ResponseEntity.ok(basketService.save(basket));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBasket(@PathVariable Long id, @Validated @RequestBody Basket basketDetails) {
        if (basketDetails.getProducts() == null) {
            return ResponseEntity.badRequest().body("Le panier ne peut pas être null");
        }
        return basketService.updateBasket(id, basketDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBasket(@PathVariable Long id) {
        if (basketService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(404).body("Le panier avec l'ID spécifié n'existe pas");
    }
}
