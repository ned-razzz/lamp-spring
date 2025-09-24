package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.dto.BookDto;
import ch.hambak.lamp.bible.dto.ChapterDto;
import ch.hambak.lamp.bible.dto.VerseDto;
import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.repository.BookRepository;
import ch.hambak.lamp.bible.repository.ChapterRepository;
import ch.hambak.lamp.bible.repository.VerseRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class BibleDomainServiceImpl implements BibleDomainService {

    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final VerseRepository verseRepository;

    @Override
    public Verse findVerseEntityById(Long verseId) {
        return verseRepository.findById(verseId)
                .orElseThrow(() -> new EntityNotFoundException("Verse not found with id: " + verseId));
    }

    @Override
    public BookDto findBook(String abbr) {
        return bookRepository.findByAbbr(abbr)
                .map(this::bookToDto)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with abbr: " + abbr));
    }

    @Override
    public BookDto findNextBook(int currentSequence) {
        return bookRepository.findTopBySequenceGreaterThanOrderBySequenceAsc(currentSequence)
                .or(bookRepository::findTopByOrderBySequenceAsc)
                .map(this::bookToDto)
                .orElseThrow(() -> new IllegalStateException("Cannot find any book."));
    }

    @Override
    public ChapterDto findChapter(long bookId, int ordinal) {
        return chapterRepository.findByBookIdAndOrdinal(bookId, ordinal)
                .map(this::chapterToDto)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
    }

    @Override
    public int countVerses(long chapterId) {
        return verseRepository.countVersesByChapter(chapterId);
    }

    @Override
    public VerseDto findVerse(long bookId, long chapterId, int ordinal) {
        return verseRepository.findByBookAndChapterAndVerse(bookId, chapterId, ordinal)
                .map(this::verseToDto)
                .orElseThrow(() -> new IllegalArgumentException("Verse not found"));
    }

    @Override
    public List<VerseDto> findVersesFrom(long bookId, long chapterId, int startOrdinal, int endOrdinal) {
        return verseRepository.findVersesFrom(bookId, chapterId, startOrdinal, endOrdinal)
                .stream()
                .map(this::verseToDto)
                .toList();
    }

    @Override
    public VerseDto findNextVerse(long verseId) {
        Verse verse = verseRepository.findById(verseId)
                .orElseThrow(() -> new IllegalArgumentException("Verse not found with id: " + verseId));
        Chapter chapter = verse.getChapter();
        Book book = chapter.getBook();

        // 1. Find next verse in the same chapter
        Optional<Verse> nextVerse = verseRepository.findByBookAndChapterAndVerse(
                book.getId(), chapter.getId(), verse.getOrdinal() + 1);
        if (nextVerse.isPresent()) {
            return verseToDto(nextVerse.get());
        }

        // 2. If not found, find the first verse of the next chapter
        Optional<Chapter> nextChapter = chapterRepository.findByBookIdAndOrdinal(book.getId(), chapter.getOrdinal() + 1);
        if (nextChapter.isPresent()) {
            return verseRepository.findByBookAndChapterAndVerse(book.getId(), nextChapter.get().getId(), 1)
                    .map(this::verseToDto)
                    .orElseThrow(() -> new IllegalStateException("Verse not found in the next chapter"));
        }

        // 3. If not found, find the first verse of the first chapter of the next book
        Book nextBook = bookRepository.findTopBySequenceGreaterThanOrderBySequenceAsc(book.getSequence())
                .or(bookRepository::findTopByOrderBySequenceAsc)
                .orElseThrow(() -> new IllegalStateException("Next book not found"));

        return chapterRepository.findByBookIdAndOrdinal(nextBook.getId(), 1)
                .flatMap(firstChapter -> verseRepository.findByBookAndChapterAndVerse(nextBook.getId(), firstChapter.getId(), 1))
                .map(this::verseToDto)
                .orElseThrow(() -> new IllegalStateException("Verse not found in the next book"));
    }

    @Override
    public VerseDto findEndVerseOfRange(long verseId, int verseCount, int leftThreshold) {
        Verse currentVerse = verseRepository.findById(verseId)
                .orElseThrow(() -> new IllegalArgumentException("Verse not found with id: " + verseId));

        Chapter currentChapter = currentVerse.getChapter();
        Book currentBook = currentChapter.getBook();
        int lastVerseOrdinal = verseRepository.countVersesByChapter(currentChapter.getId());

        int targetOrdinal = currentVerse.getOrdinal() + verseCount - 1;

        // If the remaining verses are within the threshold, return the last verse of the chapter
        if (lastVerseOrdinal - currentVerse.getOrdinal() <= leftThreshold) {
            return verseRepository.findByBookAndChapterAndVerse(currentBook.getId(), currentChapter.getId(), lastVerseOrdinal)
                    .map(this::verseToDto)
                    .orElseThrow(); // Should not happen
        }

        // Find the target verse
        return verseRepository.findByBookAndChapterAndVerse(currentBook.getId(), currentChapter.getId(), targetOrdinal)
                .or(() -> verseRepository.findByBookAndChapterAndVerse(currentBook.getId(), currentChapter.getId(), lastVerseOrdinal))
                .map(this::verseToDto)
                .orElseThrow(); // Should not happen
    }

    private BookDto bookToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .abbrKor(book.getAbbrKor())
                .abbrEng(book.getAbbrEng())
                .sequence(book.getSequence())
                .build();
    }

    private ChapterDto chapterToDto(Chapter chapter) {
        return ChapterDto.builder()
                .id(chapter.getId())
                .ordinal(chapter.getOrdinal())
                .build();
    }

    private VerseDto verseToDto(Verse verse) {
        return VerseDto.builder()
                .id(verse.getId())
                .ordinal(verse.getOrdinal())
                .text(verse.getText())
                .build();
    }
}