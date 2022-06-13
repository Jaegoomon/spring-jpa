package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @OneToOne(
            fetch = FetchType.LAZY,
            mappedBy = "delivery",
            cascade = CascadeType.ALL
    )
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP

}
