package ru.morozov.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import ru.morozov.messages.NotEnoughProductMsg;
import ru.morozov.messages.ProductReservedMsg;
import ru.morozov.product.ProductMapper;
import ru.morozov.product.dto.NewProductDto;
import ru.morozov.product.dto.ProductDto;
import ru.morozov.product.entity.Product;
import ru.morozov.product.entity.Status;
import ru.morozov.product.exceptions.NotFoundException;
import ru.morozov.product.producer.ProductProducer;
import ru.morozov.product.repo.ProductRepository;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductProducer productProducer;

    public ProductDto create(NewProductDto product) {
        Assert.hasText(product.getName(), "Empty name");
        Assert.isTrue(!productRepository.findOneByName(product.getName()).isPresent(), "Name already exists");

        if (!StringUtils.hasText(product.getStatus())) {
            product.setStatus(Status.ACTIVE.name());
        }

        return ProductMapper.convertProductToProductDto(
                productRepository.save(
                        ProductMapper.convertNewProductDtoToProduct(product)
                )
        );
    }

    /**
     * Добавить/уменьшить кол-во товара
     * @param id идентификатор товара
     * @param qnt кол-во товара. Может быть как положительным, так и отрицательным значением
     * @return описание товара
     */
    public ProductDto addQnt(Long id, Integer qnt) {
        Assert.notNull(qnt, "Qnt is null");

        Optional<Product> res = productRepository.findById(id);
        if (res.isPresent()) {
            Product product = res.get();

            //проверки при уменьшении товара
            if (qnt < 0) {
                Assert.isTrue(product.getFreeQnt() >= -qnt, "Not enough free products. ProductID=" + product.getId() + ", FreeQnt=" + product.getFreeQnt());
            }

            product.setQuantity(product.getQuantity() + qnt);
            productRepository.save(product);

            return ProductMapper.convertProductToProductDto(product);
        } else {
            throw new NotFoundException(id);
        }
    }

    public void sold(Map<Long, Integer> productsQnt) {
        productsQnt.entrySet().forEach(i -> sold(i.getKey(), i.getValue()));
    }

    private ProductDto sold(Long id, Integer qnt) {
        Assert.notNull(qnt, "Qnt is null");
        Assert.isTrue(qnt.intValue() > 0, "Wrong qnt");

        Optional<Product> res = productRepository.findById(id);
        if (res.isPresent()) {
            Product product = res.get();
            Assert.isTrue(product.getReserved() >= qnt, "Not enough reserved products. ProductID=" + product.getId() + ", ReservedQnt=" + product.getReserved());

            product.setQuantity(product.getQuantity() - qnt);
            product.setReserved(product.getReserved() - qnt);
            productRepository.save(product);

            return ProductMapper.convertProductToProductDto(product);
        } else {
            throw new NotFoundException(id);
        }
    }

    public void reserve(Long orderId, Map<Long, Integer> productsQnt) {
        productsQnt.entrySet().forEach(i -> reserve(orderId, i.getKey(), i.getValue()));
        productProducer.sendProductReservedMessage(new ProductReservedMsg(orderId));
    }

    private ProductDto reserve(Long orderId, Long id, Integer qnt) {
        Assert.notNull(qnt, "Qnt is null");
        Assert.isTrue(qnt.intValue() > 0, "Wrong qnt");

        Optional<Product> res = productRepository.findById(id);
        if (res.isPresent()) {
            Product product = res.get();
            if (product.getFreeQnt() < qnt) {
                productProducer.sendNotEnoughProductMessage(new NotEnoughProductMsg(orderId));
                throw new IllegalArgumentException("Not enough free products. ProductID=" + product.getId() + ", FreeQnt=" + product.getFreeQnt());
            }

            product.setReserved(product.getReserved() + qnt);
            productRepository.save(product);

            return ProductMapper.convertProductToProductDto(product);
        } else {
            throw new NotFoundException(id);
        }
    }

    public void release(Long orderId, Map<Long, Integer> productsQnt) {
        productsQnt.entrySet().forEach(i -> release(orderId, i.getKey(), i.getValue()));
    }

    private ProductDto release(Long orderId, Long id, Integer qnt) {
        Assert.notNull(qnt, "Qnt is null");
        Assert.isTrue(qnt.intValue() > 0, "Wrong qnt");

        Optional<Product> res = productRepository.findById(id);
        if (res.isPresent()) {
            Product product = res.get();
            Assert.isTrue(product.getReserved() >= qnt, "Not enough reserved products. ProductID=" + product.getId() + ", ReservedQnt=" + product.getReserved());

            product.setReserved(product.getReserved() - qnt);
            productRepository.save(product);

            return ProductMapper.convertProductToProductDto(product);
        } else {
            throw new NotFoundException(id);
        }
    }

    public ProductDto setPrice(Long id, Float price) {
        Assert.notNull(price, "Price is null");
        Assert.isTrue(price.floatValue() > 0, "Wrong price");

        Optional<Product> res = productRepository.findById(id);
        if (res.isPresent()) {
            Product product = res.get();

            product.setPrice(price);
            productRepository.save(product);

            return ProductMapper.convertProductToProductDto(product);
        } else {
            throw new NotFoundException(id);
        }
    }

    public ProductDto get(Long id) {
        Optional<Product> res = productRepository.findById(id);
        if (res.isPresent()) {
            Product product = res.get();
            return ProductMapper.convertProductToProductDto(product);
        } else {
            throw new NotFoundException(id);
        }
    }
}
