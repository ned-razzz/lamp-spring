package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;

import java.util.List;

public interface BibleDomainService {

    /**
     * ID를 이용해 영속 상태의 Verse 엔티티를 조회합니다.
     * 이 메서드는 다른 도메인에서 연관관계를 설정해야 할 때만 사용되어야 합니다.
     * @param verseId 조회할 Verse의 ID
     * @return 영속 상태의 Verse 엔티티
     */
    Verse findVerseEntityById(Long verseId);

    Book findBook(String abbr);

    Book findNextBook(int currentSequence);

    Chapter findChapter(long bookId, int ordinal);

    int countVerses(long chapterId);

    Verse findVerse(long bookId, long chapterId, int ordinal);
    Verse findVerse(String bookAbbr, int chapterOrdinal, int verseOrdinal);

    List<Verse> findVersesFrom(long bookId, long chapterId, int startOrdinal, int endOrdinal);
    List<Verse> findVersesFrom(String bookAbbr, int chapterOrdinal, int startOrdinal, int endOrdinal);

    Verse findNextVerse(Verse verse);

    Verse findEndVerseOfRange(Verse startVerse, int verseCount, int leftThreshold);
}