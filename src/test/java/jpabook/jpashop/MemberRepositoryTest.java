package jpabook.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    void testMember() {
        Member member = new Member();
        member.setUsername("member");

        Long savedId = memberRepository.save(member);

        Member targetMember = memberRepository.find(savedId);

        assertThat(targetMember.getId()).isEqualTo(member.getId());
        assertThat(targetMember.getUsername()).isEqualTo(member.getUsername());
    }
}