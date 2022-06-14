package jpabook.jpashop.repository.order.collection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryCollectionRepository {

    private final EntityManager em;

    public List<OrderQueryCollectionDto> findOrderQueryCollectionDtos() {
        List<OrderQueryCollectionDto> orders = findOrders();
        orders.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getId());
            o.setOrderItems(orderItems);
        });
        return orders;
    }

    public List<OrderQueryCollectionDto> findOrderQueryCollectionDtosV2() {
        List<OrderQueryCollectionDto> orders = findOrders();
        List<Long> orderIds = orders.stream()
                .map(OrderQueryCollectionDto::getId)
                .collect(Collectors.toList());

        String query = "select new jpabook.jpashop.repository.order.collection.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                "from OrderItem as oi " +
                "join oi.item as i " +
                "where oi.order.id in :orderIds";
        List<OrderItemQueryDto> orderItems = em.createQuery(query, OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        orders.forEach(o -> o.setOrderItems(orderItemMap.get(o.getId())));
        return orders;
    }

    private List<OrderItemQueryDto> findOrderItems(Long id) {
        String query = "select new jpabook.jpashop.repository.order.collection.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                "from OrderItem as oi " +
                "join oi.item as i " +
                "where oi.order.id = :orderId";
        return em.createQuery(query, OrderItemQueryDto.class)
                .setParameter("orderId", id)
                .getResultList();
    }

    public List<OrderQueryCollectionDto> findOrders() {
        String query = "select new jpabook.jpashop.repository.order.collection.OrderQueryCollectionDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                "from Order as o " +
                "join o.member as m " +
                "join o.delivery as d";
        return em.createQuery(query, OrderQueryCollectionDto.class)
                .getResultList();
    }
}
