package ru.morozov.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.morozov.messages.OrderDoneMsg;
import ru.morozov.messages.SagaReserveProductMsg;
import ru.morozov.messages.SagaReserveProductRollbackMsg;
import ru.morozov.product.service.MessageService;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @Autowired
    private MessageService messageService;

    @Value("${mq.SagaReserveProduct-topic}")
    private String sagaReserveProductTopic;

    @Value("${mq.SagaReserveProductRollback-topic}")
    private String sagaReserveProductRollbackTopic;

    @Value("${mq.ProductSold-topic}")
    private String productSoldTopic;

    @PostMapping("/sendSagaReserveProductMsg")
    public void sendSagaReserveProductMsg(@RequestBody SagaReserveProductMsg message) {
        messageService.scheduleSentMessage(sagaReserveProductTopic, null, message, SagaReserveProductMsg.class);
    }

    @PostMapping("/sendSagaReserveProductRollbackMsg")
    public void sendSagaReserveProductRollbackMsg(@RequestBody SagaReserveProductRollbackMsg message) {
        messageService.scheduleSentMessage(sagaReserveProductRollbackTopic, null, message, SagaReserveProductRollbackMsg.class);
    }

    @PostMapping("/sendProductSoldMsg")
    public void sendProductSoldMsg(@RequestBody OrderDoneMsg message) {
        messageService.scheduleSentMessage(productSoldTopic, null, message, OrderDoneMsg.class);
    }
}
