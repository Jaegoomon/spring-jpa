package jpabook.jpashop.repository.order.collection;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public OrderQueryCollectionDto(List<OrderFlatDto> orderFlatDtos) {
        this.id = orderFlatDtos.get(0).getId();
        this.name = orderFlatDtos.get(0).getName();
        this.orderDate = orderFlatDtos.get(0).getOrderDate();
        this.orderStatus = orderFlatDtos.get(0).getOrderStatus();
        this.address = orderFlatDtos.get(0).getAddress();
        this.orderItems = orderFlatDtos.stream()
                .map(o -> new OrderItemQueryDto(o.getId(), o.getItemName(), o.getOrderPrice(), o.getCount()))
                .collect(Collectors.toList());
    }
}
