package ch.hambak.lamp.daily_bible.repository;

import ch.hambak.lamp.daily_bible.entity.GlobalReadingPlan;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReadingPlanRepositoryImpl implements ReadingPlanRepository {
    private final EntityManager em;

    public Long save(GlobalReadingPlan readingPlan) {
        em.persist(readingPlan);
        return readingPlan.getId();
    }

    public Optional<GlobalReadingPlan> findWithAllBibleEntity() {
        String jpql = """
                SELECT p
                FROM GlobalReadingPlan p
                JOIN FETCH p.startVerse sv
                JOIN FETCH sv.chapter sc
                JOIN FETCH sc.book sb
                JOIN FETCH p.endVerse ev
                JOIN FETCH ev.chapter ec
                JOIN FETCH ec.book eb
                """;
        return em.createQuery(jpql, GlobalReadingPlan.class)
                .getResultStream()
                .findFirst();
    }
}
