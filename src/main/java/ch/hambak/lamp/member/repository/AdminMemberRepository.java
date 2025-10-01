package ch.hambak.lamp.member.repository;

import ch.hambak.lamp.member.entity.AdminMember;

import java.util.Optional;

public interface AdminMemberRepository {
    Optional<AdminMember> findById(Long id);
    Optional<AdminMember> findByEmailAndStatusPending(String email);
}
