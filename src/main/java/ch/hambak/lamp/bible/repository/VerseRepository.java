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

    public Optional<Verse> findByBibleReference(Short bookId, Short chapter, Short verseIndex) {
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

    public List<Verse> findByBibleReferenceRange(Short bookId, Short chapter, Short startIndex, Short endIndex) {
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
}
