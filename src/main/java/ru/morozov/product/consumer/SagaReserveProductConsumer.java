package ru.morozov.product.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.morozov.messages.SagaReserveProductMsg;
import ru.morozov.product.repo.RedisRepository;
import ru.morozov.product.service.ProductService;

@Component
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = "${mq.SagaReserveProduct-topic}")
public class SagaReserveProductConsumer {

    private final ProductService productService;
    private final RedisRepository redisRepository;

    @RabbitHandler
    public void receive(SagaReserveProductMsg msg) {
        log.info("Received Message: {}", msg.toString());

        String idempotenceKey = "SagaReserveProduct_" + msg.getOrderId().toString();
        log.info("idempotenceKey={}", idempotenceKey);

        //idempotence check
        Object value = redisRepository.find(idempotenceKey);
        if (value != null) {
            log.info("Order has already been processed. IdempotenceKey=" + idempotenceKey);
            return;
        } else {
            redisRepository.add(idempotenceKey, idempotenceKey);
        }

        try {
            productService.reserve(msg.getOrderId(), msg.getProductsQnt());
        } catch (Exception e) {
            log.error("Failed to save product", e);
        }
    }
}
