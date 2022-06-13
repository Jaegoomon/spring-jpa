package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> search(OrderSearch orderSearch) {

        if (orderSearch.getMemberName() == "") {
            orderSearch.setMemberName(null);
        }

        String query = "select o from Order o join o.member m ";

        if (orderSearch.getMemberName() != null && orderSearch.getOrderStatus() != null) {
            query += "where o.status = :status ";
            query += "and m.name like :name ";

            return em.createQuery(query, Order.class)
                    .setParameter("status", orderSearch.getOrderStatus())
                    .setParameter("name", orderSearch.getMemberName())
                    .setMaxResults(1000)
                    .getResultList();
        } else if (orderSearch.getMemberName() == null && orderSearch.getOrderStatus() != null) {
            query += "where o.status = :status";

            return em.createQuery(query, Order.class)
                    .setParameter("status", orderSearch.getOrderStatus())
                    .setMaxResults(1000)
                    .getResultList();
        } else if (orderSearch.getMemberName() != null && orderSearch.getOrderStatus() == null) {
            query += "where m.name like :name";

            return em.createQuery(query, Order.class)
                    .setParameter("name", orderSearch.getMemberName())
                    .setMaxResults(1000)
                    .getResultList();
        } else {
            return em.createQuery(query, Order.class)
                    .setMaxResults(1000)
                    .getResultList();
        }
    }
}
