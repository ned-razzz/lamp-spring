package ch.hambak.lamp.daily_bible;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.repository.BookRepository;
import ch.hambak.lamp.bible.repository.ChapterRepository;
import ch.hambak.lamp.bible.repository.VerseRepository;
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
public class DailyBibleService {
    private final ReadingPlanRepository readingPlanRepository;

    //todo: 다른 도메인의 repository 의존을 service 의존으로 변경
    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final VerseRepository verseRepository;

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

//        Long verseSize = verseRepository.countByBookIdAndChapter(currentBook.getId(), currentVerse.getChapter());
//        int restVerseSize = (int) (verseSize - currentVerse.getIndex());
//
//        int endVerseIndex = (short) (currentVerse.getIndex() + readingPlan.getAmountPerDay() - 1);
//        //todo: ReadingPlan에 남은 구절 처리 칼럼 추가하기
//        if (restVerseSize < readingPlan.getAmountPerDay() + 3) {
//            endVerseIndex += restVerseSize;
//        }

        List<Verse> verses = verseRepository.findVersesFrom(
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
                .chapter(currentVerse.getChapter().getOrdinal())
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
        Verse endVerse = verseRepository.findLastVerseFrom(
                        currentBook.getId(),
                        currentChapter.getId(),
                        currentVerse.getOrdinal(),
                        currentVerse.getOrdinal() + amountPerDay - 1)
                .orElseThrow();

        // 다음 날 시작할 절을 찾습니다.
        Verse nextVerse = findNextVerse(endVerse);

        readingPlan.update(nextVerse, null);
    }

    //todo: 해당 코드 bible 도메인에 이전
    public Verse findNextVerse(Verse verse) {
        Chapter chapter = verse.getChapter();
        Book book = chapter.getBook();

        Optional<Verse> nextVerse1 = verseRepository.findByBookAndChapterAndVerse(
                book.getId(),
                chapter.getId(),
                verse.getOrdinal() + 1);
        if (nextVerse1.isPresent()) {
            return nextVerse1.get();
        }

        Optional<Chapter> nextChapter = chapterRepository.findByBookIdAndOrdinal(book.getId(), chapter.getOrdinal() + 1);
        if (nextChapter.isPresent()) {
            return verseRepository.findByBookAndChapterAndVerse(
                    book.getId(),
                    nextChapter.get().getId(),
                    1).orElseThrow();
        }

        //todo: 해당 코드 BookService 메서드로 이전
        Book nextBook = bookRepository.findTopBySequenceGreaterThanOrderBySequenceAsc(book.getSequence())
                .orElseGet(() -> bookRepository.findTopByOrderBySequenceAsc().orElseThrow());
        Chapter nextBookChapter = chapterRepository.findByBookIdAndOrdinal(nextBook.getId(), 1).orElseThrow();

        return verseRepository.findByBookAndChapterAndVerse(
                nextBook.getId(),
                nextBookChapter.getId(),
                1).orElseThrow();
    }


    /**
     * 관리자가 전체 읽기 진도의 현재 위치와 하루 분량을 강제로 설정합니다.
     * @param updateRequest 관리자가 설정할 책, 장, 절, 하루 분량 정보를 담은 DTO
     */
    @Transactional
    public void updatePlan(ReadingPlanUpdateRequest updateRequest) {
        GlobalReadingPlan readingPlan = readingPlanRepository.find().orElseThrow();
        Book book = bookRepository.findByAbbr(updateRequest.getBookAbbr()).orElseThrow();
        Chapter chapter = chapterRepository.findByBookIdAndOrdinal(book.getId(), updateRequest.getChapter()).orElseThrow();

        log.info("data = {} {} {}", book.getId(), chapter.getId(), updateRequest.getVerse());
        Verse verse = verseRepository.findByBookAndChapterAndVerse(
                        book.getId(),
                        chapter.getId(),
                        updateRequest.getVerse())
                .orElseThrow();

        readingPlan.update(verse, updateRequest.getCountPerDay());
    }
}
