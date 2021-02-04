package ru.morozov.product;

import ru.morozov.product.dto.NewProductDto;
import ru.morozov.product.dto.ProductDto;
import ru.morozov.product.entity.Product;

public class ProductMapper {

    public static ProductDto convertProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setStatus(product.getStatus());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setReserved(product.getReserved());

        return productDto;
    }

    public static Product convertNewProductDtoToProduct(NewProductDto newProductDto) {
        Product product = new Product();
        product.setName(newProductDto.getName());
        product.setDescription(newProductDto.getDescription());
        product.setStatus(newProductDto.getStatus());
        product.setQuantity(0);
        product.setReserved(0);

        return product;
    }

    public static Product convertProductDtoToProduct(ProductDto productDto) {
        Product product = convertNewProductDtoToProduct(productDto);
        product.setId(productDto.getId());

        return product;
    }
}
