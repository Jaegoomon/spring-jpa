package jpabook.jpashop.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BookForm extends ItemForm {

    @NotEmpty(message = "작가 이름은 필수입니다.")
    private String author;

    private String isbn;

}
