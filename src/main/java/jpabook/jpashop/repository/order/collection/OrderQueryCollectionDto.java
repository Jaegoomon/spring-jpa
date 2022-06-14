package jpabook.jpashop.repository.order.collection;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderQueryCollectionDto {

    private final Long id;
    private final String name;
    private final LocalDateTime orderDate;
    private final OrderStatus orderStatus;
    private final Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryCollectionDto(Long id, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.id = id;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
