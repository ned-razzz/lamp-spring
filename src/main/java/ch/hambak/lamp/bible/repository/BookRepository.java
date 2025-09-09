package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Book;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//todo: 모든 JPQL 문 대문자 처리하기
@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final EntityManager em;

    public Short save(Book book) {
        em.persist(book);
        return book.getId();
    }

    public List<Book> findAll() {
        return em.createQuery("select b from Book b", Book.class)
                .getResultList();
    }

    public Optional<Book> findById(Short id) {
        Book book = em.find(Book.class, id);
        return Optional.ofNullable(book);
    }

    public Optional<Book> findByAbbr(String abbr) {
        String jpql = "select b from Book b where b.abbrEng = :abbr";
        return em.createQuery(jpql, Book.class)
                .setParameter("abbr", abbr)
                .getResultStream()
                .findFirst();
    }

    /**
     * 현재 bookOrder보다 큰 값 중 가장 작은 bookOrder를 가진 Book을 찾습니다.
     * @param bookOrder 현재 책의 순서
     * @return 다음 책 (Optional)
     */
    public Optional<Book> findTopByBookOrderGreaterThanOrderByBookOrderAsc(Short bookOrder) {
        String jpql = """
                SELECT b FROM Book b
                WHERE b.bookOrder > :bookOrder
                ORDER BY b.bookOrder ASC
                """;
        return em.createQuery(jpql, Book.class)
                .setParameter("bookOrder", bookOrder)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }

    /**
     * 가장 작은 bookOrder를 가진 Book을 찾습니다.
     * @return 첫 번째 책 (Optional)
     */
    public Optional<Book> findTopByOrderByBookOrderAsc() {
        String jpql = """
                SELECT b FROM Book b
                ORDER BY book_order ASC
                """;
        return em.createQuery(jpql, Book.class)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }
}
