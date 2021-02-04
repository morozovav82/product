package ru.morozov.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewProductDto {
    private String name;
    private String description;
    private String status;
}
