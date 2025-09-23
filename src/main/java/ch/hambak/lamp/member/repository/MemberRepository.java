package ch.hambak.lamp.member.repository;

import ch.hambak.lamp.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Long save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
}
