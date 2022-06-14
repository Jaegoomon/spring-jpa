package jpabook.jpashop.repository.order.collection;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFlatDto {

    private final Long id;
    private final String name;
    private final LocalDateTime orderDate;
    private final OrderStatus orderStatus;
    private final Address address;
    private final String itemName;
    private final int orderPrice;
    private final int count;

}
