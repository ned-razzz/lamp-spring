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
        //todo: 성능 최적화 위해서 verse, chapter, book 관련 데이터까지 한번에 호출
        return em.createQuery("select p from GlobalReadingPlan p", GlobalReadingPlan.class)
                .getResultStream()
                .findFirst();
    }
}
