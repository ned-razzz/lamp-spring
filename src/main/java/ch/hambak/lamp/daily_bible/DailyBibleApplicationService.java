package ch.hambak.lamp.daily_bible;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.service.BibleDomainService;
import ch.hambak.lamp.daily_bible.dto.DailyVerseResponse;
import ch.hambak.lamp.daily_bible.dto.ReadingPlanUpdateRequest;
import ch.hambak.lamp.daily_bible.dto.TodayBibleResponse;
import ch.hambak.lamp.daily_bible.entity.GlobalReadingPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DailyBibleApplicationService {
    private final ReadingPlanRepository readingPlanRepository;

    private final BibleDomainService bibleService;

    /**
     * 오늘 읽어야 할 성경 구절을 조회합니다.
     * - 현재 챕터에 남은 절이 9절 이하이면 남은 절 전체를 반환합니다.
     * - 그렇지 않으면 관리자가 설정한 하루 분량(versesPerDay)만큼 반환합니다.
     * @return 오늘의 성경 구절 정보를 담은 DTO
     */
    public TodayBibleResponse readTodayVerses() {
        GlobalReadingPlan readingPlan = readingPlanRepository.find().orElseThrow();
        Verse currentVerse = readingPlan.getVerse();
        Chapter currentChapter = currentVerse.getChapter();
        Book currentBook = currentChapter.getBook();

        List<Verse> verses = bibleService.findVersesFrom(
                currentBook.getId(),
                currentChapter.getId(),
                currentVerse.getOrdinal(),
                (currentVerse.getOrdinal() + readingPlan.getAmountPerDay() - 1));

        List<DailyVerseResponse> verseDataList = verses.stream()
                .map(verse -> DailyVerseResponse.builder()
                        .ordinal(verse.getOrdinal())
                        .text(verse.getText())
                        .build())
                .toList();

        return TodayBibleResponse.builder()
                .bookName(currentBook.getName())
                .chapterOrdinal(currentVerse.getChapter().getOrdinal())
                .verses(verseDataList)
                .build();
    }

    /**
     * 읽기 진도를 다음 날로 업데이트합니다.
     * - getTodaysReading() 로직에 따라 오늘 읽은 분량을 계산합니다.
     * - 마지막으로 읽은 구절을 기준으로 다음 시작점을 찾습니다.
     * - 절 -> 장 -> 책 순으로 넘어가며, 성경 전체를 다 읽으면 창세기로 순환합니다.
     * - 이 메소드는 스케줄러에 의해 매일 자정에 호출되어야 합니다.
     */
    @Transactional
    public void advanceToNextDay() {
        GlobalReadingPlan readingPlan = readingPlanRepository.find().orElseThrow();
        Verse currentVerse = readingPlan.getVerse();
        Chapter currentChapter = currentVerse.getChapter();
        Book currentBook = currentChapter.getBook();
        Integer amountPerDay = readingPlan.getAmountPerDay();

        // 오늘 읽은 마지막 절을 찾습니다.
        Verse endVerse = bibleService.findVerse(
                        currentBook.getId(),
                        currentChapter.getId(),
                        currentVerse.getOrdinal() + amountPerDay - 1)
                // endOrdinal이 절 수를 초과했다면 장의 마지막 절을 반환
                .or(() -> bibleService.findVerse(
                        currentBook.getId(),
                        currentChapter.getId(),
                        bibleService.countVerses(currentChapter.getId())))
                .orElseThrow();

        // 다음 날 시작할 절을 찾습니다.
        Verse nextVerse = bibleService.findNextVerse(endVerse);
        readingPlan.update(nextVerse, null);
    }

    /**
     * 관리자가 전체 읽기 진도의 현재 위치와 하루 분량을 강제로 설정합니다.
     * @param updateRequest 관리자가 설정할 책, 장, 절, 하루 분량 정보를 담은 DTO
     */
    @Transactional
    public void updatePlan(ReadingPlanUpdateRequest updateRequest) {
        GlobalReadingPlan readingPlan = readingPlanRepository.find().orElseThrow();
        Book book = bibleService.findBook(updateRequest.getBookAbbr()).orElseThrow();
        Chapter chapter = bibleService.findChapter(book.getId(), updateRequest.getChapterOrdinal()).orElseThrow();
        Verse verse = bibleService.findVerse(
                        book.getId(),
                        chapter.getId(),
                        updateRequest.getVerseOrdinal())
                .orElseThrow();
        readingPlan.update(verse, updateRequest.getCountPerDay());
    }
}
