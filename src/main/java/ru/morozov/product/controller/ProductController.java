package ru.morozov.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.morozov.product.dto.NewProductDto;
import ru.morozov.product.dto.ProductDto;
import ru.morozov.product.exceptions.NotFoundException;
import ru.morozov.product.service.ProductService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@RequestBody NewProductDto product) {
        return productService.create(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> info(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity(
                    productService.get(id),
                    HttpStatus.OK
            );
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/addQnt")
    public ResponseEntity<ProductDto> addQnt(@PathVariable("id") Long id, @RequestParam Integer qnt) {
        try {
            return new ResponseEntity(
                    productService.addQnt(id, qnt),
                    HttpStatus.OK
            );
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/setPrice")
    public ResponseEntity<ProductDto> setPrice(@PathVariable("id") Long id, @RequestParam Float price) {
        try {
            return new ResponseEntity(
                    productService.setPrice(id, price),
                    HttpStatus.OK
            );
        } catch (NotFoundException e) {
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
