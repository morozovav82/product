package ru.morozov.product.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.morozov.messages.SagaReserveProductMsg;
import ru.morozov.product.service.ProductService;

@Component
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = "${active-mq.SagaReserveProduct-topic}")
public class SagaReserveProductConsumer {

    private final ProductService productService;

    @RabbitHandler
    public void receive(SagaReserveProductMsg msg) {
        log.info("Received Message: {}", msg.toString());
        try {
            productService.reserve(msg.getOrderId(), msg.getProductsQnt());
        } catch (Exception e) {
            log.error("Failed to save product", e);
        }
    }
}
