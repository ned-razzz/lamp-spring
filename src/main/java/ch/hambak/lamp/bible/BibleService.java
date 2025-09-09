package ch.hambak.lamp.bible;

import ch.hambak.lamp.bible.dto.BookResponse;
import ch.hambak.lamp.bible.dto.VerseResponse;
import ch.hambak.lamp.bible.entity.Book;
import ch.hambak.lamp.bible.entity.Verse;
import ch.hambak.lamp.bible.repository.BookRepository;
import ch.hambak.lamp.bible.repository.VerseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BibleService {

    private final BookRepository bookRepository;
    private final VerseRepository verseRepository;

    public BookResponse readBook(String abbr) {
        Book book = bookRepository.findByAbbr(abbr).orElseThrow();
        return BookResponse.builder()
                .name(book.getName())
                .abbrKor(book.getAbbrKor())
                .abbrEng(book.getAbbrEng())
                .bookOrder(book.getBookOrder())
                .build();
    }

    public VerseResponse readVerse(String abbr, Short chapter, Short verseIndex) {
        Verse verse = verseRepository.findByBibleReference(abbr, chapter, verseIndex).orElseThrow();
        return VerseResponse.builder()
                .verse(verse.getIndex())
                .text(verse.getText())
                .build();
    }

    public List<VerseResponse> readVersesRange(String abbr, Short chapter, Short startIndex, Short endIndex) {
        List<Verse> verses = verseRepository.findByBibleReferenceRange(abbr, chapter, startIndex, endIndex);
        return verses.stream()
                .map(verse -> VerseResponse.builder()
                        .verse(verse.getIndex())
                        .text(verse.getText())
                        .build())
                .toList();
    }
}
