package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

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
        Address address = new Address("고양시", "백양로", "10504");
        Member member = createMember("Jho", address);

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
        Address address = new Address("고양시", "백양로", "10504");
        Member member = createMember("Jho", address);

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
        Address address = new Address("고양시", "백양로", "10504");
        Member member = createMember("Jho", address);

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

    @Test
    public void 상품검색() {
        // given
        int orderCount = 1;

        Address address1 = new Address("고양시", "백양로", "10504");
        Member member1 = createMember("Jho", address1);
        Item book1 = createBook("Test Book1", 12000, 10);
        Item book2 = createBook("Test Book2", 13000, 7);

        Long order1 = orderService.order(member1.getId(), book1.getId(), orderCount);
        Long order2 = orderService.order(member1.getId(), book2.getId(), orderCount);

        Address address2 = new Address("대전광역시", "대학로291", "34101");
        Member member2 = createMember("Goo", address2);
        Item book3 = createBook("Test Book3", 11000, 10);
        Item book4 = createBook("Test Book4", 9000, 8);

        Long order3 = orderService.order(member2.getId(), book3.getId(), orderCount);
        Long order4 = orderService.order(member2.getId(), book4.getId(), orderCount);
        orderService.cancel(order4);

        // when
        OrderSearch orderSearch1 = new OrderSearch();
        orderSearch1.setMemberName(member1.getName());
        orderSearch1.setOrderStatus(OrderStatus.ORDER);

        List<Order> orderList1 = orderService.search(orderSearch1);

        OrderSearch orderSearch2 = new OrderSearch();
        orderSearch2.setMemberName(member2.getName());
        orderSearch2.setOrderStatus(OrderStatus.ORDER);

        List<Order> orderList2 = orderService.search(orderSearch2);

        // then
        assertThat(orderList1.size()).isEqualTo(2);
        assertThat(orderList2.size()).isEqualTo(1);
    }

    private Member createMember(String name, Address address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
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