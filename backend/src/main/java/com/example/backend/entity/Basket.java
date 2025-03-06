package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "baskets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int userId;

    @OneToMany
    private List<Product> products;
}
