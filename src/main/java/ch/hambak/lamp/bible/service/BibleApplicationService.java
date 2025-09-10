package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.dto.BookResponse;
import ch.hambak.lamp.bible.dto.VerseResponse;
import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Chapter;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.repository.BookRepository;
import ch.hambak.lamp.bible.repository.ChapterRepository;
import ch.hambak.lamp.bible.repository.VerseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BibleApplicationService {

    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final VerseRepository verseRepository;

    public BookResponse readBook(String abbr) {
        Book book = bookRepository.findByAbbr(abbr).orElseThrow();
        return BookResponse.builder()
                .name(book.getName())
                .abbrKor(book.getAbbrKor())
                .abbrEng(book.getAbbrEng())
                .bookOrder(book.getSequence())
                .build();
    }

    public VerseResponse readVerse(String abbr, int chapterOrdinal, int verseOrdinal) {
        Book book = bookRepository.findByAbbr(abbr).orElseThrow();
        Chapter chapter = chapterRepository.findByBookIdAndOrdinal(book.getId(), chapterOrdinal).orElseThrow();
        Verse verse = verseRepository.findByBookAndChapterAndVerse(book.getId(), chapter.getId(), verseOrdinal).orElseThrow();
        return VerseResponse.builder()
                .verse(verse.getOrdinal())
                .text(verse.getText())
                .build();
    }

    public List<VerseResponse> readVersesRange(String abbr, int chapterOrdinal, int startVerseOrdinal, int endVerseOrdinal) {
        Book book = bookRepository.findByAbbr(abbr).orElseThrow();
        Chapter chapter = chapterRepository.findByBookIdAndOrdinal(book.getId(), chapterOrdinal).orElseThrow();
        List<Verse> verses = verseRepository.findVersesFrom(
                book.getId(),
                chapter.getId(),
                startVerseOrdinal,
                endVerseOrdinal);
        return verses.stream()
                .map(verse -> VerseResponse.builder()
                        .verse(verse.getOrdinal())
                        .text(verse.getText())
                        .build())
                .toList();
    }
}
