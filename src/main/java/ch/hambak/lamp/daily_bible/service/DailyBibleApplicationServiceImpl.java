package ch.hambak.lamp.daily_bible.service;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.service.BibleDomainService;
import ch.hambak.lamp.daily_bible.dto.*;
import ch.hambak.lamp.daily_bible.entity.GlobalReadingPlan;
import ch.hambak.lamp.daily_bible.repository.ReadingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DailyBibleApplicationServiceImpl implements DailyBibleApplicationService {
    private final ReadingPlanRepository readingPlanRepository;
    private final BibleDomainService bibleService;

    @Override
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
    @Override
    public void advanceToNextDay() {
        GlobalReadingPlan readingPlan = readingPlanRepository.findWithAllBibleEntity().orElseThrow();
        Verse nextStartVerse = bibleService.findNextVerse(readingPlan.getEndVerse());
        Verse nextEndVerse = bibleService.findEndVerseOfRange(nextStartVerse, readingPlan.getAmountPerDay(), readingPlan.getThreshold());
        readingPlan.updateRange(nextStartVerse, nextEndVerse);
        log.info("daily bible moves on to the next day");
    }

    @Transactional
    @Override
    public void createPlan(ReadingPlanCreateRequest createRequest) {
        // find start verse and end verse
        BibleIndex bibleIndex = createRequest.getBibleIndex();
        Verse startVerse = bibleService.findVerse(
                bibleIndex.getBookAbbr(),
                bibleIndex.getChapterOrdinal(),
                bibleIndex.getVerseOrdinal());
        Verse endVerse = bibleService.findEndVerseOfRange(startVerse, createRequest.getAmountPerDay(), createRequest.getThreshold());

        // insert reading plan record
        GlobalReadingPlan readingPlan = GlobalReadingPlan.create(
                startVerse,
                endVerse,
                createRequest.getAmountPerDay(),
                createRequest.getThreshold());
        readingPlanRepository.save(readingPlan);
    }

    @Transactional
    @Override
    public void updatePlan(ReadingPlanUpdateRequest updateRequest) {
        GlobalReadingPlan readingPlan = readingPlanRepository.findWithAllBibleEntity()
                .orElseThrow(() -> new NoSuchElementException("Global reading plan does not exist"));

        // update detail columns
        readingPlan.updateDetails(updateRequest.getAmountPerDay(), updateRequest.getThreshold());

        // update verse range columns
        BibleIndex bibleIndex = updateRequest.getBibleIndex();
        if (bibleIndex != null) {
            // find start verse
            Verse startVerse = bibleService.findVerse(
                    bibleIndex.getBookAbbr(),
                    bibleIndex.getChapterOrdinal(),
                    bibleIndex.getVerseOrdinal());

            // find end verse
            Integer count = (updateRequest.getAmountPerDay() != null) ? updateRequest.getAmountPerDay() : readingPlan.getAmountPerDay();
            Integer threshold = (updateRequest.getThreshold() != null) ? updateRequest.getThreshold() : readingPlan.getThreshold();
            Verse endVerse = bibleService.findEndVerseOfRange(startVerse, count, threshold);

            readingPlan.updateRange(startVerse, endVerse);
        }
    }
}
