package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() {
        // given
        Member member = new Member();
        member.setName("Jho");
        // when
        Long savedId = memberService.join(member);
        // then
        assertThat(memberRepository.findOne(savedId)).isEqualTo(member);
    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("Jho");

        Member member2 = new Member();
        member2.setName("Jho");
        // when
        memberService.join(member1);
        // then
        assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
    }
}