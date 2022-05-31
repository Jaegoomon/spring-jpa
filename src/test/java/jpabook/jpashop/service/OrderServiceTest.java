package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void 상품주문() {
        // given
        Member member = createMember();

        int price = 12000;
        int stockQuantity = 10;
        Item book = createBook("Test Book", price, stockQuantity);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order order = orderRepository.findOne(orderId);

        // then
        assertEquals(OrderStatus.ORDER, order.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(stockQuantity - orderCount, book.getStockQuantity(), "주문 수량만큼 재고 감소");
        assertEquals(1, order.getOrderItems().size(), "주문한 상품 종류 수 1개");
        assertEquals(price * orderCount, order.getTotalPrice(), "주문 가격은 가격 * 수량");
    }

    @Test
    public void 상품주문_제고수량초과() {
        // given
        Member member = createMember();

        int price = 12000;
        int stockQuantity = 10;
        Item book = createBook("Test Book", price, stockQuantity);

        // when
        int orderCount = 11;
        assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), book.getId(), orderCount));

        // then
    }

    @Test
    public void 주문취소() {
        // given
        Member member = createMember();

        int price = 12000;
        int stockQuantity = 10;
        Item book = createBook("Test Book", price, stockQuantity);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order order = orderRepository.findOne(orderId);
        order.cancel();

        // then
        assertEquals(OrderStatus.CANCEL, order.getStatus(), "상품 주문시 상태는 CANCEL");
        assertEquals(stockQuantity, book.getStockQuantity(), "주문 원래 수량만큼 재고 복구");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("Jho");
        member.setAddress(new Address("고양시", "백양로", "10504"));
        em.persist(member);
        return member;
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}