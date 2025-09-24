package ch.hambak.lamp.member.service;

import ch.hambak.lamp.member.dto.MemberCreateRequest;

public interface MemberApplicationService {
    void registerMember(MemberCreateRequest createRequest);
    void deleteMember(Long id);
}
