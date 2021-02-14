package ru.morozov.product.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.morozov.messages.NotEnoughProductMsg;
import ru.morozov.messages.ProductReservedMsg;
import ru.morozov.product.service.MessageService;

@Component
@Slf4j
public class ProductProducer {

    @Autowired
    private MessageService messageService;

    @Value("${active-mq.ProductReserved-topic}")
    private String productReservedTopic;

    @Value("${active-mq.NotEnoughProduct-topic}")
    private String notEnoughProductTopic;

    public void sendProductReservedMessage(ProductReservedMsg message){
        messageService.scheduleSentMessage(productReservedTopic, null, message, ProductReservedMsg.class);
    }

    public void sendNotEnoughProductMessage(NotEnoughProductMsg message){
        messageService.scheduleSentMessage(notEnoughProductTopic, null, message, NotEnoughProductMsg.class);
    }
}