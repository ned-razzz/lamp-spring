package ch.hambak.lamp.daily_bible.service;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.service.BibleDomainService;
import ch.hambak.lamp.daily_bible.repository.ReadingPlanRepository;
import ch.hambak.lamp.daily_bible.dto.DailyVerseResponse;
import ch.hambak.lamp.daily_bible.dto.ReadingPlanUpdateRequest;
import ch.hambak.lamp.daily_bible.dto.TodayBibleResponse;
import ch.hambak.lamp.daily_bible.entity.GlobalReadingPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DailyBibleApplicationServiceImpl implements DailyBibleApplicationService {
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
        Verse startVerse = readingPlan.getStartVerse();
        Verse endVerse = readingPlan.getEndVerse();
        Chapter currentChapter = startVerse.getChapter();
        Book currentBook = currentChapter.getBook();

        List<Verse> verses = bibleService.findVersesFrom(
                currentBook.getId(),
                currentChapter.getId(),
                startVerse.getOrdinal(),
                endVerse.getOrdinal());

        List<DailyVerseResponse> verseDataList = verses.stream()
                .map(verse -> DailyVerseResponse.builder()
                        .ordinal(verse.getOrdinal())
                        .text(verse.getText())
                        .build())
                .toList();

        return TodayBibleResponse.builder()
                .bookName(currentBook.getName())
                .chapterOrdinal(currentChapter.getOrdinal())
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
    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Seoul")
    @Transactional
    public void advanceToNextDay() {
        GlobalReadingPlan readingPlan = readingPlanRepository.find().orElseThrow();
        Verse endVerse = readingPlan.getEndVerse();
        Verse nextStartVerse = bibleService.findNextVerse(endVerse);
        Verse nextEndVerse = findVerseAfter(nextStartVerse, readingPlan.getCountPerDay());
        readingPlan.update(nextStartVerse, nextEndVerse, null, null);

        log.info("daily bible moves on to the next day");
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
        Verse startVerse = bibleService.findVerse(
                        book.getId(),
                        chapter.getId(),
                        updateRequest.getVerseOrdinal())
                .orElseThrow();

        //끝 절 찯기
        Integer count = Objects.requireNonNullElse(updateRequest.getCountPerDay(), readingPlan.getCountPerDay());
        Verse endVerse = findVerseAfter(startVerse, count);

        readingPlan.update(startVerse, endVerse, updateRequest.getCountPerDay(), updateRequest.getVersesLeftThreshold());
    }

    //todo: bible service로 이동
    private Verse findVerseAfter(Verse currentVerse, int offset) {
        GlobalReadingPlan readingPlan = readingPlanRepository.find().orElseThrow();
        Chapter currentChapter = currentVerse.getChapter();
        Book currentBook = currentChapter.getBook();
        int lastVerseOrdinal = bibleService.countVerses(currentChapter.getId());

        Optional<Verse> afterVerse = bibleService.findVerse(currentBook.getId(),
                currentChapter.getId(),
                currentVerse.getOrdinal() + offset - 1);

        // 성경 읽기 범위의 끝 절이 해당 장의 절 길이를 초과했을 시, 장의 마지막 절 반환
        if (afterVerse.isEmpty()) {
            return bibleService.findVerse(currentBook.getId(),
                            currentChapter.getId(),
                            lastVerseOrdinal)
                    .orElseThrow();
        }

        // 해당 장의 남은 절의 수가 3개 이하일 시 그냥 남은 절까지 전부 범위에 포함시킨다
        if (lastVerseOrdinal - currentVerse.getOrdinal() <= readingPlan.getVersesLeftThreshold()) {
            if (afterVerse.get().getOrdinal() != lastVerseOrdinal) {
                return bibleService.findVerse(currentBook.getId(),
                                currentChapter.getId(),
                                lastVerseOrdinal)
                        .orElseThrow();
            }
        }

        return afterVerse.get();
    }
}
