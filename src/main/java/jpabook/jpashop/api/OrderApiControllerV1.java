package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/api/v1/orders")
    public List<Order> listOrderV1() {
        List<Order> orders = orderRepository.search(new OrderSearch());
        return orders;
    }
}
