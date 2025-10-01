package ch.hambak.lamp.member.repository;

import ch.hambak.lamp.member.entity.AdminMember;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminMemberRepositoryImpl implements AdminMemberRepository {

    private final EntityManager em;

    @Override
    public Optional<AdminMember> findById(Long id) {
        AdminMember member = em.find(AdminMember.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<AdminMember> findByEmailAndStatusPending(String email) {
        String jpql = """
                SELECT m
                FROM AdminMember m
                WHERE m.email = :email
                AND m.status = PENDING
                """;
        return em.createQuery(jpql, AdminMember.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
}
