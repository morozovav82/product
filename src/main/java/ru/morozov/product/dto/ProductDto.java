package ru.morozov.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto extends NewProductDto {
    private Long id;
    private Float price;
    private Integer quantity;
    private Integer reserved;

    public Integer getFreeQnt() {
        return quantity - reserved;
    }
}
