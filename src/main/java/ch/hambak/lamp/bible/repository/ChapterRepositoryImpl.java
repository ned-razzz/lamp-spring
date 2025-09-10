package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Chapter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChapterRepositoryImpl implements ChapterRepository {

    private final EntityManager em;

    public Long save(Chapter chapter) {
        em.persist(chapter);
        return chapter.getId();
    }

    public Optional<Chapter> findById(Long id) {
        Chapter chapter = em.find(Chapter.class, id);
        return Optional.ofNullable(chapter);
    }

    public Optional<Chapter> findByBookIdAndOrdinal(Long bookId, int ordinal) {
        String jpql = """
                SELECT c
                FROM Chapter c
                WHERE c.book.id = :bookId
                AND c.ordinal = :ordinal
                """;
        return em.createQuery(jpql, Chapter.class)
                .setParameter("bookId", bookId)
                .setParameter("ordinal", ordinal)
                .getResultStream()
                .findFirst();
    }

    public List<Chapter> findByBookId(Long bookId) {
        String jpql = """
                SELECT c
                FROM Chapter c
                WHERE c.book.id = :bookId
                """;
        return em.createQuery(jpql, Chapter.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    public Integer countByBookId(Long bookId) {
        String jpql = """
                SELECT COUNT(c)
                FROM Chapter c
                WHERE c.book.id = :bookId
                """;
        return em.createQuery(jpql, Integer.class)
                .setParameter("bookId", bookId)
                .getSingleResult();
    }

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
