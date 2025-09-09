package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Verse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VerseRepository {

    private final EntityManager em;

    public Long save(Verse verse) {
        em.persist(verse);
        return verse.getId();
    }

    public Optional<Verse> findById(Long id) {
        Verse verse = em.find(Verse.class, id);
        return Optional.ofNullable(verse);
    }

    public Optional<Verse> findByBookAndChapterAndVerse(Short bookId, Short chapter, Short verseIndex) {
        String jpql = """
                select v
                from Verse v
                where v.book.id = :bookId
                and v.chapter = :chapter
                and v.index = :verse
                """;
        return em.createQuery(jpql, Verse.class)
                .setParameter("bookId", bookId)
                .setParameter("chapter", chapter)
                .setParameter("verse", verseIndex)
                .getResultStream()
                .findFirst();
    }

    public List<Verse> findVersesFrom(Short bookId, Short chapter, Short startIndex, Short endIndex) {
        String jpql = """
                select v
                from Verse v
                where v.book.id = :bookId
                and v.chapter = :chapter
                and v.index between :startVerse and :endVerse
                order by v.index asc
                """;

        return em.createQuery(jpql, Verse.class)
                .setParameter("bookId", bookId)
                .setParameter("chapter", chapter)
                .setParameter("startVerse", startIndex)
                .setParameter("endVerse", endIndex)
                .getResultList();
    }

    public Optional<Verse> findLastVerseFrom(Short bookId, Short chapter, Short startIndex, Short endIndex) {
        String jpql = """
                SELECT v FROM Verse v
                WHERE v.book.id = :bookId
                AND v.chapter = :chapter
                AND v.index between :startVerse and :endVerse
                ORDER BY v.index DESC
                """;
        return em.createQuery(jpql, Verse.class)
                .setParameter("bookId", bookId)
                .setParameter("chapter", chapter)
                .setParameter("startVerse", startIndex)
                .setParameter("endVerse", endIndex)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }

    public Long countByBookIdAndChapter(Short bookId, int chapter) {
        String jpql = """
                select count(v)
                from Verse v
                where v.book.id = :bookId
                and v.chapter = :chapter
                """;
        return em.createQuery(jpql, Long.class)
                .setParameter("bookId", bookId)
                .setParameter("chapter", chapter)
                .getSingleResult();
    }
}
