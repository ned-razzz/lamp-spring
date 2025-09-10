package ch.hambak.lamp.bible.service;

import ch.hambak.lamp.bible.dto.BookResponse;
import ch.hambak.lamp.bible.dto.VerseResponse;

import java.util.List;

public interface BibleApplicationService {

    BookResponse readBook(String abbr);

    VerseResponse readVerse(String abbr, int chapterOrdinal, int verseOrdinal);

    List<VerseResponse> readVersesRange(String abbr, int chapterOrdinal, int startVerseOrdinal, int endVerseOrdinal);
}