package ru.morozov.product.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import ru.morozov.messages.NotEnoughProductMsg;
import ru.morozov.messages.ProductReservedMsg;

@Component
@Slf4j
public class ProductProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${active-mq.ProductReserved-topic}")
    private String productReservedTopic;

    @Value("${active-mq.NotEnoughProduct-topic}")
    private String notEnoughProductTopic;

    private void sendMessage(String topic, Object message){
        try{
            log.info("Attempting send message to Topic: "+ topic);
            jmsTemplate.convertAndSend(topic, message);
            log.info("Message sent: {}", message);
        } catch(Exception e){
            log.error("Failed to send message", e);
        }
    }

    public void sendProductReservedMessage(ProductReservedMsg message){
        sendMessage(productReservedTopic, message);
    }

    public void sendNotEnoughProductMessage(NotEnoughProductMsg message){
        sendMessage(notEnoughProductTopic, message);
    }
}