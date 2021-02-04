package ru.morozov.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import ru.morozov.messages.SagaReserveProductMsg;
import ru.morozov.messages.SagaReserveProductRollbackMsg;

@RestController
@RequestMapping("/product/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${active-mq.SagaReserveProduct-topic}")
    private String sagaReserveProductTopic;

    @Value("${active-mq.SagaReserveProductRollback-topic}")
    private String sagaReserveProductRollbackTopic;

    private void sendMessage(String topic, Object message){
        try{
            log.info("Attempting send message to Topic: "+ topic);
            jmsTemplate.convertAndSend(topic, message);
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
}
