package ru.morozov.product.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Value("${active-mq.SagaReserveProduct-topic}")
    private String sagaReserveProductTopic;

    @Value("${active-mq.SagaReserveProductRollback-topic}")
    private String sagaReserveProductRollbackTopic;

    @Value("${active-mq.OrderDone-exchange}")
    private String orderDoneExchange;

    @Value("${active-mq.ProductSold-topic}")
    private String productSoldTopic;

    @Value("${active-mq.ProductReserved-topic}")
    private String productReservedTopic;

    @Value("${active-mq.NotEnoughProduct-topic}")
    private String notEnoughProductTopic;

    @Bean
    public Queue sagaReserveProductQueue() {
        return new Queue(sagaReserveProductTopic);
    }

    @Bean
    public Queue sagaReserveProductRollbackQueue() {
        return new Queue(sagaReserveProductRollbackTopic);
    }

    @Bean
    public Queue productSoldQueue() {
        return new Queue(productSoldTopic);
    }

    @Bean
    public Queue productReservedQueue() {
        return new Queue(productReservedTopic);
    }

    @Bean
    public Queue notEnoughProductQueue() {
        return new Queue(notEnoughProductTopic);
    }

    @Bean
    TopicExchange orderDoneExchange() {
        return new TopicExchange(orderDoneExchange);
    }

    @Bean
    Binding binding(@Qualifier("productSoldQueue") Queue queue, @Qualifier("orderDoneExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("default");
    }
}
