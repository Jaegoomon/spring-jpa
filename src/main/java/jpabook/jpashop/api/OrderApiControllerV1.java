package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.order.qeury.OrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.qeury.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderApiControllerV1 {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> listOrderV1() {
        List<Order> orders = orderRepository.search(new OrderSearch());
        return orders;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> listOrderV2() {
        List<Order> orders = orderRepository.search(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> listOrderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> listOrderV4() {
        return orderQueryRepository.findOrderDtos();
    }

    @Data
    static class OrderDto {

        private final Long id;
        private final String name;
        private final LocalDateTime orderDate;
        private final OrderStatus orderStatus;
        private final Address address;

        public OrderDto(Order order) {
            id = order.getId();
            name = order.getMember().getName(); // LAZY
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY
        }
    }
}
