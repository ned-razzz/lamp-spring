package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;

import java.util.List;
import java.util.Optional;

public interface BibleDomainService {

    Optional<Book> findBook(String abbr);

    Optional<Book> findNextBook(int currentSequence);

    Optional<Chapter> findChapter(long bookId, int ordinal);

    int countVerses(long chapterId);

    Optional<Verse> findVerse(long bookId, long chapterId, int ordinal);

    List<Verse> findVersesFrom(long bookId, long chapterId, int startOrdinal, int endOrdinal);

    Verse findNextVerse(Verse verse);

    Verse findEndVerseOfRange(Verse currentVerse, int offset, int threshold);
}
