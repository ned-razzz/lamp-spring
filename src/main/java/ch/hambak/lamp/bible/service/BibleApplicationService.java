package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.dto.BookResponse;
import ch.hambak.lamp.bible.dto.VerseResponse;

import java.util.List;

public interface BibleApplicationService {

    /**
     * 성경 책의 정보를 조회합니다.
     * @param abbr 성경 책의 약어 (e.g., "gen", "exo")
     * @return 성경 책 정보를 담은 DTO
     */
    BookResponse readBook(String abbr);

    /**
     * 특정 성경 구절을 조회합니다.
     * @param abbr 성경 책의 약어
     * @param chapterOrdinal 장 번호
     * @param verseOrdinal 절 번호
     * @return 성경 구절 정보를 담은 DTO
     */
    VerseResponse readVerse(String abbr, int chapterOrdinal, int verseOrdinal);

    /**
     * 특정 범위의 성경 구절들을 조회합니다.
     * @param abbr 성경 책의 약어
     * @param chapterOrdinal 장 번호
     * @param startVerseOrdinal 시작 절 번호
     * @param endVerseOrdinal 끝 절 번호
     * @return 성경 구절 정보 목록을 담은 DTO 리스트
     */
    List<VerseResponse> readVersesRange(String abbr, int chapterOrdinal, int startVerseOrdinal, int endVerseOrdinal);
}