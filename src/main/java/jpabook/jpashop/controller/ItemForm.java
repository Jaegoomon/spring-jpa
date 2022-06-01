package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class ItemForm {

    @NotEmpty(message = "물건 이름은 필수입니다.")
    private String name;
    @NotNull(message = "물건 가격은 필수입니다.")
    private int price;
    @NotNull(message = "재고 수량은 필수입니다.")
    private int stockQuantity;

}
