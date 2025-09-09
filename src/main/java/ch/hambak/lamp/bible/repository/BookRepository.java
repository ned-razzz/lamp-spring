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
}
