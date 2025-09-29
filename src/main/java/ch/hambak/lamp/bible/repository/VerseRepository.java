package ch.hambak.lamp.bible.repository;

import ch.hambak.lamp.bible.entity.Verse;

import java.util.List;
import java.util.Optional;

public interface VerseRepository {

    Long save(Verse verse);

    Optional<Verse> findById(Long id);

    Optional<Verse> findByBookAndChapterAndVerse(Long bookId, Long chapterId, Integer ordinal);

    Optional<Verse> findByBibleIndex(String abbr, Integer chapterOrdinal, Integer verseOrdinal);

    List<Verse> findVersesFrom(Long bookId, Long chapterId, Integer startOrdinal, Integer endOrdinal);

    List<Verse> findVersesFrom(String abbr, Integer chapterOrdinal, Integer startOrdinal, Integer endOrdinal);

    Integer countVersesByChapter(long chapterId);
}
