package ch.hambak.lamp.member;

import ch.hambak.lamp.member.dto.MemberCreateRequest;
import ch.hambak.lamp.member.service.MemberApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/member")
@Validated
@RequiredArgsConstructor
public class MemberController {
    private final MemberApplicationService memberApplicationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createMember(@RequestBody @Valid MemberCreateRequest createRequest) {
        memberApplicationService.requestMembership(createRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/admin/{email}")
    public void approveMember(@PathVariable @Email String email) {
        memberApplicationService.approveMembership(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/admin/{memberId}")
    public void deleteMember(@PathVariable Long memberId) {
        memberApplicationService.deleteMember(memberId);
    }
}
