package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Chapter;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository {

    Long save(Chapter chapter);

    Optional<Chapter> findById(Long id);

    Optional<Chapter> findByBookIdAndOrdinal(Long bookId, int ordinal);

    List<Chapter> findByBookId(Long bookId);

    Integer countByBookId(Long bookId);

    Integer countVersesByChapter(long chapterId);
}