package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> listMembersV1() {
        return memberService.findAll();
    }

    @GetMapping("/api/v2/members")
    public ListMemberResponse listMembersV2() {
        List<Member> findMembers = memberService.findAll();
        List<MemberDto> collect = findMembers.stream()
                .map(member -> new MemberDto(member))
                .collect(Collectors.toList());

        return new ListMemberResponse(collect);
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());
        member.setName(request.getName());
        member.setAddress(address);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());

        Member result = memberService.findOne(id);
        return new UpdateMemberResponse(result.getId(), result.getName());
    }

    @Data
    static class ListMemberResponse<T extends List> {

        private final int count;
        private final T data;

        public ListMemberResponse(T data) {
            this.count = data.size();
            this.data = data;
        }
    }

    @Data
    static class MemberDto {

        private String name;

        public MemberDto(Member member) {
            this.name = member.getName();
        }
    }

    @Data
    static class CreateMemberRequest {

        @NotEmpty
        private String name;
        @NotEmpty
        private String city;
        @NotEmpty
        private String street;
        @NotEmpty
        private String zipcode;

    }

    @Data
    static class CreateMemberResponse {

        private final Long id;

    }

    @Data
    static class UpdateMemberRequest {

        @NotEmpty
        private String name;

    }

    @Data
    static class UpdateMemberResponse {

        private final Long id;
        private final String name;

    }
}
