package ru.morozov.messages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@ToString
public class SagaReserveProductMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Map<Long, Integer> productsQnt; //ProductId -> Qnt
}
