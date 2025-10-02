package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;

import java.util.List;
import java.util.Optional;

public interface BibleDomainService {

    /**
     * ID를 이용해 영속 상태의 Verse 엔티티를 조회합니다.
     * 이 메서드는 다른 도메인에서 연관관계를 설정해야 할 때만 사용되어야 합니다.
     *
     * @param verseId 조회할 Verse의 ID
     * @return 영속 상태의 Verse 엔티티
     */
    Optional<Verse> findVerseEntityById(Long verseId);

    /**
     * 약어를 이용해 성경 책을 조회합니다。
     *
     * @param abbr 성경 책의 약어 (e.g., "gen", "exo")
     * @return Book 엔티티
     */
    Optional<Book> findBook(String abbr);

    /**
     * 현재 책의 순서를 기준으로 다음 책을 조회합니다。
     *
     * @param currentSequence 현재 책의 순서
     * @return 다음 Book 엔티티. 요한계시록 다음은 창세기를 반환합니다。
     */
    Optional<Book> findNextBook(int currentSequence);

    /**
     * 책 ID와 장 번호를 이용해 장을 조회합니다。
     *
     * @param bookId  책의 ID
     * @param ordinal 장 번호
     * @return Chapter 엔티티
     */
    Optional<Chapter> findChapter(long bookId, int ordinal);

    /**
     * 특정 장에 포함된 모든 절의 개수를 계산합니다。
     * @param chapterId 장의 ID
     * @return 절의 개수
     */
    int countVerses(long chapterId);

    /**
     * 책 ID, 장 ID, 절 번호를 이용해 특정 절을 조회합니다。
     *
     * @param bookId    책의 ID
     * @param chapterId 장의 ID
     * @param ordinal   절 번호
     * @return Verse 엔티티
     */
    Optional<Verse> findVerse(long bookId, long chapterId, int ordinal);

    /**
     * 책 약어, 장 번호, 절 번호를 이용해 특정 절을 조회합니다。
     *
     * @param bookAbbr       책의 약어
     * @param chapterOrdinal 장 번호
     * @param verseOrdinal   절 번호
     * @return Verse 엔티티
     */
    Optional<Verse> findVerse(String bookAbbr, int chapterOrdinal, int verseOrdinal);

    /**
     * 책 ID, 장 ID, 시작 및 끝 절 번호를 이용해 절의 범위를 조회합니다。
     * @param bookId 책의 ID
     * @param chapterId 장의 ID
     * @param startOrdinal 시작 절 번호
     * @param endOrdinal 끝 절 번호
     * @return Verse 엔티티 리스트
     */
    List<Verse> findVersesFrom(long bookId, long chapterId, int startOrdinal, int endOrdinal);

    /**
     * 책 약어, 장 번호, 시작 및 끝 절 번호를 이용해 절의 범위를 조회합니다。
     * @param bookAbbr 책의 약어
     * @param chapterOrdinal 장 번호
     * @param startOrdinal 시작 절 번호
     * @param endOrdinal 끝 절 번호
     * @return Verse 엔티티 리스트
     */
    List<Verse> findVersesFrom(String bookAbbr, int chapterOrdinal, int startOrdinal, int endOrdinal);

    /**
     * 주어진 절을 기준으로 다음 절을 찾습니다。
     * 장이나 책의 마지막 절일 경우, 다음 장 또는 다음 책의 첫 번째 절을 반환합니다。
     *
     * @param verse 기준이 되는 Verse 엔티티
     * @return 다음 Verse 엔티티
     */
    Optional<Verse> findNextVerse(Verse verse);

    /**
     * 시작 절로부터 특정 개수만큼의 절을 읽을 때, 마지막 절을 결정합니다。
     * 장의 끝에 가까워져 남은 절의 수가 임계값(leftThreshold) 이하일 경우, 해당 장의 마지막 절까지를 범위의 끝으로 설정합니다。
     * @param startVerse 시작 절
     * @param verseCount 읽을 절의 개수
     * @param leftThreshold 장의 마지막 절까지 읽을지를 결정하는 임계값
     * @return 범위의 마지막 Verse 엔티티
     */
    Verse findEndVerseOfRange(Verse startVerse, int verseCount, int leftThreshold);
}