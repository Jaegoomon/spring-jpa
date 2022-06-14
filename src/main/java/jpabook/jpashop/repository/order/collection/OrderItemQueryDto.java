package jpabook.jpashop.repository.order.collection;

import lombok.Data;

@Data
public class OrderItemQueryDto {

    private final Long orderId;
    private final String itemName;
    private final int orderPrice;
    private final int count;

}
