package ch.hambak.lamp.daily_bible.service;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.service.BibleDomainService;
import ch.hambak.lamp.daily_bible.dto.DailyVerseResponse;
import ch.hambak.lamp.daily_bible.dto.ReadingPlanUpdateRequest;
import ch.hambak.lamp.daily_bible.dto.TodayBibleResponse;
import ch.hambak.lamp.daily_bible.entity.GlobalReadingPlan;
import ch.hambak.lamp.daily_bible.repository.ReadingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DailyBibleApplicationServiceImpl implements DailyBibleApplicationService {
    private final ReadingPlanRepository readingPlanRepository;
    private final BibleDomainService bibleService;

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

    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Seoul")
    @Transactional
    public void advanceToNextDay() {
        GlobalReadingPlan readingPlan = readingPlanRepository.find().orElseThrow();
        Verse nextStartVerse = bibleService.findNextVerse(readingPlan.getEndVerse());
        Verse nextEndVerse = bibleService.findEndVerseOfRange(nextStartVerse, readingPlan.getAmountPerDay(), readingPlan.getThreshold());
        readingPlan.update(nextStartVerse, nextEndVerse, null, null);
        log.info("daily bible moves on to the next day");
    }

    @Transactional
    public void updatePlan(ReadingPlanUpdateRequest updateRequest) {
        GlobalReadingPlan readingPlan = readingPlanRepository.find().orElseThrow();
        Verse startVerse = bibleService.findVerse(
                updateRequest.getBookAbbr(),
                updateRequest.getChapterOrdinal(),
                updateRequest.getVerseOrdinal());

            //끝 절 찯기
            Integer count = (updateRequest.getAmountPerDay() != null) ? updateRequest.getAmountPerDay() : readingPlan.getAmountPerDay();
            Integer threshold = (updateRequest.getThreshold() != null) ? updateRequest.getThreshold() : readingPlan.getThreshold();
            Verse endVerse = bibleService.findEndVerseOfRange(startVerse, count, threshold);

        readingPlan.update(startVerse, endVerse, updateRequest.getCountPerDay(), updateRequest.getVersesLeftThreshold());
    }
}
