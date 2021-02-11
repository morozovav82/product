package ru.morozov.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.morozov.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findOneByName(String name);
    List<Product> findByStatus(String status);

    @Query("select p from products p where p.status=:status and (p.name like %:name% or p.description like %:description%)")
    List<Product> search(@Param("name") String name, @Param("description") String description, @Param("status") String status);
}
