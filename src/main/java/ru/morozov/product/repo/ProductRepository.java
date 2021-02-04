package ru.morozov.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.morozov.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findOneByName(String name);
    List<Product> findByStatus(String status);
}
