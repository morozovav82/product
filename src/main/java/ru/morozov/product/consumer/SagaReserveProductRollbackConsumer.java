package ru.morozov.product.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.morozov.messages.SagaReserveProductRollbackMsg;
import ru.morozov.product.service.ProductService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class SagaReserveProductRollbackConsumer implements MessageListener {

    private final ProductService productService;

    private ObjectMessage receiveMessage(Message message) {
        ObjectMessage objectMessage;

        try {
            objectMessage = (ObjectMessage) message;
            log.info("Received Message: {}", objectMessage.getObject().toString());
            return objectMessage;
        } catch (Exception e) {
            log.error("Failed to receive message", e);
            return null;
        }
    }

    @Override
    @JmsListener(destination = "${active-mq.SagaReserveProductRollback-topic}")
    public void onMessage(Message message) {
        ObjectMessage objectMessage = receiveMessage(message);
        if (objectMessage == null) return;

        try {
            SagaReserveProductRollbackMsg msg = (SagaReserveProductRollbackMsg) objectMessage.getObject();
            productService.release(msg.getProductsQnt());
        } catch (Exception e) {
            log.error("Failed to save products", e);
        }
    }
}
