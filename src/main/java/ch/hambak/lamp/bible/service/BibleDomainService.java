package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.dto.BookDto;
import ch.hambak.lamp.bible.dto.ChapterDto;
import ch.hambak.lamp.bible.dto.VerseDto;
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

    BookDto findBook(String abbr);

    BookDto findNextBook(int currentSequence);

    ChapterDto findChapter(long bookId, int ordinal);

    int countVerses(long chapterId);

    VerseDto findVerse(long bookId, long chapterId, int ordinal);

    List<VerseDto> findVersesFrom(long bookId, long chapterId, int startOrdinal, int endOrdinal);

    VerseDto findNextVerse(long verseId);

    VerseDto findEndVerseOfRange(long verseId, int verseCount, int leftThreshold);
}