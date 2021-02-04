package ru.morozov.product.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @SequenceGenerator(name="products_gen", sequenceName="products_id_seq", allocationSize = 1)
    @GeneratedValue(strategy=SEQUENCE, generator="products_gen")
    private Long id;

    private String name;
    private String description;
    private Float price;
    private String status;
    private Integer quantity;
    private Integer reserved;

    public Integer getFreeQnt() {
        return quantity - reserved;
    }
}
