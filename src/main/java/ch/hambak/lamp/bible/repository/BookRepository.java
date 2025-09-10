package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Book;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final EntityManager em;

    public Long save(Book book) {
        em.persist(book);
        return book.getId();
    }

    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class)
                .getResultList();
    }

    public Optional<Book> findById(Long id) {
        Book book = em.find(Book.class, id);
        return Optional.ofNullable(book);
    }

    public Optional<Book> findByAbbr(String abbr) {
        String jpql = "SELECT b FROM Book b WHERE b.abbrEng = :abbr";
        return em.createQuery(jpql, Book.class)
                .setParameter("abbr", abbr)
                .getResultStream()
                .findFirst();
    }

    /**
     * 현재 sequence보다 큰 값 중 가장 작은 sequence 가진 Book을 찾습니다.
     * @param sequence 현재 책의 순서
     * @return 다음 책 (Optional)
     */
    public Optional<Book> findTopBySequenceGreaterThanOrderBySequenceAsc(int sequence) {
        String jpql = """
                SELECT b FROM Book b
                WHERE b.sequence > :sequence
                ORDER BY b.sequence ASC
                """;
        return em.createQuery(jpql, Book.class)
                .setParameter("sequence", sequence)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }

    /**
     * 가장 작은 sequence를 가진 Book을 찾습니다.
     * @return 첫 번째 책 (Optional)
     */
    public Optional<Book> findTopByOrderBySequenceAsc() {
        String jpql = """
                SELECT b FROM Book b
                ORDER BY b.sequence ASC
                """;
        return em.createQuery(jpql, Book.class)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }
}
