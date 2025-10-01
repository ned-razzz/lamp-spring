package ch.hambak.lamp.member.service;

import ch.hambak.lamp.member.dto.MemberCreateRequest;

public interface MemberApplicationService {
    /**
     * 새로운 회원을 등록 신청합니다.
     * @param createRequest 회원 가입에 필요한 정보를 담은 DTO
     */
    void requestMembership(MemberCreateRequest createRequest);

    /**
     * 등록 신청한 회원을 승인합니다.
     * @param email 신청한 회원의 이메일
     */
    void approveMembership(String email);

    /**
     * ID를 이용해 회원을 삭제합니다.
     * @param id 삭제할 회원의 ID
     */
    void deleteMember(Long id);
}
