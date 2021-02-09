package ru.morozov.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.morozov.messages.ProductSoldMsg;
import ru.morozov.messages.SagaReserveProductMsg;
import ru.morozov.messages.SagaReserveProductRollbackMsg;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${active-mq.SagaReserveProduct-topic}")
    private String sagaReserveProductTopic;

    @Value("${active-mq.SagaReserveProductRollback-topic}")
    private String sagaReserveProductRollbackTopic;

    @Value("${active-mq.ProductSold-topic}")
    private String productSoldTopic;

    private void sendMessage(String topic, Object message){
        try{
            log.info("Attempting send message to Topic: "+ topic);
            rabbitTemplate.convertAndSend(topic, message);
            log.info("Message sent: {}", message);
        } catch(Exception e){
            log.error("Failed to send message", e);
        }
    }

    @PostMapping("/sendSagaReserveProductMsg")
    public void sendSagaReserveProductMsg(@RequestBody SagaReserveProductMsg message) {
        sendMessage(sagaReserveProductTopic, message);
    }

    @PostMapping("/sendSagaReserveProductRollbackMsg")
    public void sendSagaReserveProductRollbackMsg(@RequestBody SagaReserveProductRollbackMsg message) {
        sendMessage(sagaReserveProductRollbackTopic, message);
    }

    @PostMapping("/sendProductSoldMsg")
    public void sendProductSoldMsg(@RequestBody ProductSoldMsg message) {
        sendMessage(productSoldTopic, message);
    }
}
