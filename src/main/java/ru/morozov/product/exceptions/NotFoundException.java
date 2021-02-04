package ru.morozov.product.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("Not found by id=" + id);
    }
}
