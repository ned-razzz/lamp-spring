package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Verse;

import java.util.List;
import java.util.Optional;

public interface VerseRepository {

    Long save(Verse verse);

    Optional<Verse> findById(Long id);

    Optional<Verse> findByBookAndChapterAndVerse(Long bookId, Long chapterId, Integer ordinal);

    List<Verse> findVersesFrom(Long bookId, Long chapterId, Integer startOrdinal, Integer endOrdinal);

    Optional<Verse> findLastVerseFrom(Long bookId, Long chapterId, Integer startOrdinal, Integer endOrdinal);

    Integer countVersesByChapter(long chapterId);
}
