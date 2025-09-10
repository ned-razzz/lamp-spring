package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Long save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Optional<Book> findByAbbr(String abbr);

    Optional<Book> findTopBySequenceGreaterThanOrderBySequenceAsc(int sequence);

    Optional<Book> findTopByOrderBySequenceAsc();
}
