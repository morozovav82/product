package ru.morozov.messages;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SagaReserveProductRollbackMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Map<Long, Integer> productsQnt; //ProductId -> Qnt
}
