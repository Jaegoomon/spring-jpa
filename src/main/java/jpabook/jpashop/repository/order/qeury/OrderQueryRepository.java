package jpabook.jpashop.repository.order.qeury;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderDtos() {
        String query = "select new jpabook.jpashop.repository.order.qeury.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                "from Order as o " +
                "join o.member as m " +
                "join o.delivery as d";
        return em.createQuery(query, OrderQueryDto.class)
                .getResultList();
    }
}
