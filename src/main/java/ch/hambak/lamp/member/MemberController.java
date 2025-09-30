package ch.hambak.lamp.member;

import ch.hambak.lamp.member.dto.MemberCreateRequest;
import ch.hambak.lamp.member.service.MemberApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberApplicationService memberApplicationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin")
    public void createMember(@RequestBody @Valid MemberCreateRequest createRequest) {
        memberApplicationService.registerMember(createRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/admin/{memberId}")
    public void deleteMember(@PathVariable Long memberId) {
        memberApplicationService.deleteMember(memberId);
    }
}
