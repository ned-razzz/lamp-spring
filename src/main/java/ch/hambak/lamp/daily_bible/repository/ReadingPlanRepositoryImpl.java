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

    public Optional<GlobalReadingPlan> find() {
        return em.createQuery("select p from GlobalReadingPlan p", GlobalReadingPlan.class)
                .getResultStream()
                .findFirst();
    }
}
