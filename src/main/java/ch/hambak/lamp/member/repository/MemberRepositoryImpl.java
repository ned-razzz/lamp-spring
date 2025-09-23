package ch.hambak.lamp.member.repository;

import ch.hambak.lamp.member.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final EntityManager em;

    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String jpql = "SELECT m FROM Member m WHERE m.email = :email";
        return em.createQuery(jpql, Member.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
}
