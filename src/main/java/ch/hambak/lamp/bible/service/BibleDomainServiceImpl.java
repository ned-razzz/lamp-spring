package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.repository.BookRepositoryImpl;
import ch.hambak.lamp.bible.repository.ChapterRepositoryImpl;
import ch.hambak.lamp.bible.repository.VerseRepositoryImpl;
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
//todo: 도메인 서비스가 Entity를 반환하지 않고 DTO를 반환하도록 변경
public class BibleDomainServiceImpl implements BibleDomainService {

    private final BookRepositoryImpl bookRepository;
    private final ChapterRepositoryImpl chapterRepository;
    private final VerseRepositoryImpl verseRepository;

    public Optional<Book> findBook(String abbr) {
        return bookRepository.findByAbbr(abbr);
    }

    public Optional<Book> findNextBook(int currentSequence) {
        return bookRepository.findTopBySequenceGreaterThanOrderBySequenceAsc(currentSequence)
                .or(bookRepository::findTopByOrderBySequenceAsc);
    }

    public Optional<Chapter> findChapter(long bookId, int ordinal) {
        return chapterRepository.findByBookIdAndOrdinal(bookId, ordinal);
    }

    public int countVerses(long chapterId) {
        return chapterRepository.countVersesByChapter(chapterId);
    }

    public Optional<Verse> findVerse(long bookId, long chapterId, int ordinal) {
        return verseRepository.findByBookAndChapterAndVerse(bookId, chapterId, ordinal);
    }

    public List<Verse> findVersesFrom(long bookId, long chapterId, int startOrdinal, int endOrdinal) {
        return verseRepository.findVersesFrom(bookId, chapterId, startOrdinal, endOrdinal);
    }

    public Verse findNextVerse(Verse verse) {
        Chapter chapter = verse.getChapter();
        Book book = chapter.getBook();

        // find next verse
        Optional<Verse> nextVerse = findVerse(
                book.getId(),
                chapter.getId(),
                verse.getOrdinal() + 1);
        if (nextVerse.isPresent()) {
            return nextVerse.get();
        }

        // If the verse is last, find next verse in next chapter
        Optional<Chapter> nextChapter = findChapter(book.getId(), chapter.getOrdinal() + 1);
        if (nextChapter.isPresent()) {
            return findVerse(
                            book.getId(),
                            nextChapter.get().getId(),
                            1)
                    .orElseThrow();
        }

        // If the chapter is last, find next verse in next book
        Book nextBook = findNextBook(book.getSequence()).orElseThrow();
        Chapter nextBookChapter = findChapter(nextBook.getId(), 1).orElseThrow();
        return findVerse(
                        nextBook.getId(),
                        nextBookChapter.getId(),
                        1)
                .orElseThrow();
    }
}
