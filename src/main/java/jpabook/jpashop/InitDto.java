package jpabook.jpashop;

import com.github.javafaker.Faker;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitDto {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit() {

            Faker faker = new Faker();
            List<Member> memberList = new ArrayList<>();
            int userNum = 5;
            int itemNum = 20;

            // 멤버 추가
            for (int i = 0; i < userNum; i++) {
                Member member = new Member();
                Address address = new Address(
                        faker.address().city(),
                        faker.address().streetName(),
                        faker.address().zipCode());
                member.setName(faker.name().firstName() + faker.name().lastName());
                member.setAddress(address);

                memberList.add(member);
                em.persist(member);
            }

            // 아이템 추가
            for (int i = 0; i < itemNum; i++) {
                Book book = new Book();
                book.setName(faker.book().title());
                book.setAuthor(faker.book().author());
                book.setPrice(faker.number().numberBetween(10, 20) * 1000);
                book.setStockQuantity(faker.number().numberBetween(0, 100));
                book.setIsbn(faker.code().isbn10());

                em.persist(book);
            }

            // 주문 생성
            for (Member member: memberList) {
                int orders = (int) (Math.random() * 4) + 1;
                List<Item> items = em.createQuery("select i from Item as i", Item.class)
                        .getResultList();
                Collections.shuffle(items);
                items = items.subList(0, orders);

                List<OrderItem> collect = items.stream()
                        .map(item -> OrderItem.createOrderItem(
                                item,
                                item.getPrice(),
                                (int) (Math.random() * item.getStockQuantity() / 2 + 1)))
                        .collect(Collectors.toList());

                Delivery delivery = new Delivery();
                delivery.setAddress(member.getAddress());
                Order.createOrder(member, delivery, collect.toArray(new OrderItem[collect.size()]));
            }
        }
    }
}
