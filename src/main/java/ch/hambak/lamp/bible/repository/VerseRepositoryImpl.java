package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Verse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VerseRepositoryImpl implements VerseRepository {

    private final EntityManager em;

    public Long save(Verse verse) {
        em.persist(verse);
        return verse.getId();
    }

    public Optional<Verse> findById(Long id) {
        Verse verse = em.find(Verse.class, id);
        return Optional.ofNullable(verse);
    }

    public Optional<Verse> findByBookAndChapterAndVerse(Long bookId, Long chapterId, Integer ordinal) {
        String jpql = """
                SELECT v
                FROM Verse v JOIN v.chapter c
                WHERE c.book.id = :bookId
                AND c.id = :chapterId
                AND v.ordinal = :verse
                """;
        return em.createQuery(jpql, Verse.class)
                .setParameter("bookId", bookId)
                .setParameter("chapterId", chapterId)
                .setParameter("verse", ordinal)
                .getResultStream()
                .findFirst();
    }

    public List<Verse> findVersesFrom(Long bookId, Long chapterId, Integer startOrdinal, Integer endOrdinal) {
        String jpql = """
                SELECT v
                FROM Verse v JOIN v.chapter c
                WHERE c.book.id = :bookId
                AND c.id = :chapterId
                AND v.ordinal BETWEEN :startVerse AND :endVerse
                ORDER BY v.ordinal ASC
                """;

        return em.createQuery(jpql, Verse.class)
                .setParameter("bookId", bookId)
                .setParameter("chapterId", chapterId)
                .setParameter("startVerse", startOrdinal)
                .setParameter("endVerse", endOrdinal)
                .getResultList();
    }

    public Optional<Verse> findLastVerseFrom(Long bookId, Long chapterId, Integer startOrdinal, Integer endOrdinal) {
        String jpql = """
                SELECT v
                FROM Verse v JOIN v.chapter c
                WHERE c.book.id = :bookId
                AND c.id = :chapterId
                AND v.ordinal BETWEEN :startVerse AND :endVerse
                ORDER BY v.ordinal DESC
                """;
        return em.createQuery(jpql, Verse.class)
                .setParameter("bookId", bookId)
                .setParameter("chapterId", chapterId)
                .setParameter("startVerse", startOrdinal)
                .setParameter("endVerse", endOrdinal)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Integer countVersesByChapter(long chapterId) {
        String jpql = """
            SELECT COUNT(v)
            FROM Verse v JOIN v.chapter c
            WHERE c.id = :chapterId
            """;
        return em.createQuery(jpql, Long.class)
                .setParameter("chapterId", chapterId)
                .getSingleResult()
                .intValue();
    }
}
