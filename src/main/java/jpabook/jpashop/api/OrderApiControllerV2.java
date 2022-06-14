package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.collection.OrderQueryCollectionDto;
import jpabook.jpashop.repository.order.collection.OrderQueryCollectionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiControllerV2 {

    private final OrderRepository orderRepository;
    private final OrderQueryCollectionRepository orderQueryCollectionRepository;

    @GetMapping("/api/v1/order-collection")
    public List<Order> listOrderV1() {
        List<Order> orders = orderRepository.search(new OrderSearch());

        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            order.getOrderItems().forEach(o -> o.getItem().getName());
        }

        return orders;
    }

    @GetMapping("/api/v2/order-collection")
    public List<OrderDto> listOrderV2() {
        List<Order> orders = orderRepository.search(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3/order-collection")
    public List<OrderDto> listOrderV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3.1/order-collection")
    public List<OrderDto> listOrderV31(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v4/order-collection")
    public List<OrderQueryCollectionDto> listOrderV4() {
        return orderQueryCollectionRepository.findOrderQueryCollectionDtos();
    }

    @GetMapping("/api/v5/order-collection")
    public List<OrderQueryCollectionDto> listOrderV5() {
        return orderQueryCollectionRepository.findOrderQueryCollectionDtosV2();
    }

    @Data
    static class OrderDto {

        private final Long id;
        private final String name;
        private final LocalDateTime orderDate;
        private final OrderStatus orderStatus;
        private final Address address;
        private final List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            id = order.getId();
            name = order.getMember().getName(); // LAZY
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {

        private final String name;
        private final int orderPrice;
        private final int count;

        public OrderItemDto(OrderItem orderItem) {
            name = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
