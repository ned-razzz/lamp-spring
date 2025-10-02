package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.repository.BookRepository;
import ch.hambak.lamp.bible.repository.ChapterRepository;
import ch.hambak.lamp.bible.repository.VerseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BibleDomainServiceImpl implements BibleDomainService {

    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final VerseRepository verseRepository;

    @Override
    public Optional<Verse> findVerseEntityById(Long verseId) {
        return verseRepository.findById(verseId);
    }

    @Override
    public Optional<Book> findBook(String abbr) {
        return bookRepository.findByAbbr(abbr);
    }

    @Override
    public Optional<Book> findNextBook(int currentSequence) {
        return bookRepository.findTopBySequenceGreaterThanOrderBySequenceAsc(currentSequence)
                .or(bookRepository::findTopByOrderBySequenceAsc);
    }

    @Override
    public Optional<Chapter> findChapter(long bookId, int ordinal) {
        return chapterRepository.findByBookIdAndOrdinal(bookId, ordinal);
    }

    @Override
    public int countVerses(long chapterId) {
        return verseRepository.countVersesByChapter(chapterId);
    }

    @Override
    public Optional<Verse> findVerse(long bookId, long chapterId, int ordinal) {
        return verseRepository.findByBookAndChapterAndVerse(bookId, chapterId, ordinal);
    }

    @Override
    public Optional<Verse> findVerse(String bookAbbr, int chapterOrdinal, int verseOrdinal) {
        return verseRepository.findByBibleIndex(bookAbbr, chapterOrdinal, verseOrdinal);
    }

    @Override
    public List<Verse> findVersesFrom(long bookId, long chapterId, int startOrdinal, int endOrdinal) {
        return verseRepository.findVersesFrom(bookId, chapterId, startOrdinal, endOrdinal);
    }

    @Override
    public List<Verse> findVersesFrom(String bookAbbr, int chapterOrdinal, int startOrdinal, int endOrdinal) {
        return verseRepository.findVersesFrom(bookAbbr, chapterOrdinal, startOrdinal, endOrdinal);
    }

    @Override
    public Optional<Verse> findNextVerse(Verse verse) {
        Chapter chapter = verse.getChapter();
        Book book = chapter.getBook();

        // 1. Find next verse in the same chapter
        return verseRepository.findByBookAndChapterAndVerse(book.getId(), chapter.getId(), verse.getOrdinal()+1)
                // 2. If not found, find the first verse of the next chapter
                .or(() -> findFirstVerseInNextChapter(book, chapter))
                // 3. If not found, find the first verse of the first chapter of the next book
                .or(() -> findFirstVerseInNextBook(book));
    }

    private Optional<Verse> findFirstVerseInNextChapter(Book book, Chapter chapter) {
        return verseRepository.findByBibleIndex(book.getAbbrEng(), chapter.getOrdinal()+1, 1);
    }

    private Optional<Verse> findFirstVerseInNextBook(Book book) {
        return findNextBook(book.getSequence())
                .flatMap(nextBook -> findVerse(nextBook.getAbbrEng(), 1, 1));
    }

    @Override
    public Verse findEndVerseOfRange(Verse currentVerse, int verseCount, int leftThreshold) {
        // load chapter and book data
        Chapter currentChapter = currentVerse.getChapter();
        Book currentBook = currentChapter.getBook();

        // 1. query the last ordinal of current chapter
        int lastVerseOrdinal = verseRepository.countVersesByChapter(currentChapter.getId());

        // 2. if remained verses <= threshold, return all the verses of current chapter
        if (isRemainingLessThanThreshold(currentVerse.getOrdinal(), lastVerseOrdinal, leftThreshold)) {
            return verseRepository.findByBookAndChapterAndVerse(currentBook.getId(), currentChapter.getId(), lastVerseOrdinal)
                    .orElseThrow(() -> new IllegalStateException("Last verse not found, but it should exist."));
        }

        // 3. if remained verses > threshold, find end verse
        int endOrdinal = currentVerse.getOrdinal() + verseCount - 1;
        return verseRepository.findByBookAndChapterAndVerse(currentBook.getId(), currentChapter.getId(), endOrdinal)
                // 4. if end verse out of the current chapter,return all the verses of current chapter
                .or(() -> verseRepository.findByBookAndChapterAndVerse(currentBook.getId(), currentChapter.getId(), lastVerseOrdinal))
                .orElseThrow(() -> new IllegalStateException("End verse could not be determined."));
    }

    private boolean isRemainingLessThanThreshold(int currentOrdinal, int lastOrdinal, int threshold) {
        return (lastOrdinal - currentOrdinal) <= threshold;
    }

}