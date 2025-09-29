package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.dto.BookResponse;
import ch.hambak.lamp.bible.dto.VerseResponse;
import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BibleApplicationServiceImpl implements BibleApplicationService {

    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final VerseRepository verseRepository;

    public BookResponse readBook(String abbr) {
        Book book = bookRepository.findByAbbr(abbr)
                .orElseThrow(() -> new NoSuchElementException("Book Not Found: %s".formatted(abbr)));
        return BookResponse.builder()
                .name(book.getName())
                .abbrKor(book.getAbbrKor())
                .abbrEng(book.getAbbrEng())
                .bookOrder(book.getSequence())
                .build();
    }

    public VerseResponse readVerse(String abbr, int chapterOrdinal, int verseOrdinal) {
            Verse verse = verseRepository.findByBibleIndex(abbr, chapterOrdinal, verseOrdinal)
                    .orElseThrow(() -> new NoSuchElementException("Verse Not Found: %s %d:%d".formatted(abbr, chapterOrdinal, verseOrdinal)));
            return VerseResponse.builder()
                    .verse(verse.getOrdinal())
                    .text(verse.getText())
                    .build();
    }

    public List<VerseResponse> readVersesRange(String abbr, int chapterOrdinal, int startVerseOrdinal, int endVerseOrdinal) {
        List<Verse> verses = verseRepository.findVersesFrom(
                abbr,
                chapterOrdinal,
                startVerseOrdinal,
                endVerseOrdinal);

        if (verses.isEmpty()) {
            throw new NoSuchElementException("Verses Not Found: %s %d:%d-%d".formatted(abbr, chapterOrdinal, startVerseOrdinal, endVerseOrdinal));
        }

        return verses.stream()
                .map(verse -> VerseResponse.builder()
                        .verse(verse.getOrdinal())
                        .text(verse.getText())
                        .build())
                .toList();
    }
}
