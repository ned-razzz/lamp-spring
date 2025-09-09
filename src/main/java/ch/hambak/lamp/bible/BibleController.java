package ch.hambak.lamp.bible;

import ch.hambak.lamp.bible.dto.BookResponse;
import ch.hambak.lamp.bible.dto.VerseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bible")
@Slf4j
@RequiredArgsConstructor
public class BibleController {

    private final BibleService bibleService;

    @GetMapping("/books")
    public void getBooks() {
        log.info("GET /bible/books");
    }

    @GetMapping("/books/{abbr}")
    public ResponseEntity<BookResponse> getBook(@PathVariable String abbr) {
        log.info("GET /bible/books/{}", abbr);
        BookResponse bookDto = bibleService.readBook(abbr);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/books/{abbr}/chapters/{chapter}")
    public ResponseEntity<List<VerseResponse>> getVerses(
            @PathVariable String abbr,
            @PathVariable Short chapter,
            @RequestParam Short startVerse,
            @RequestParam Short endVerse) {
        log.info("GET /bible/books/{}/chapters/{}?startVerse={}&endVerse={}",abbr, chapter, startVerse, endVerse);
        List<VerseResponse> verseDtoList = bibleService.readVersesRange(abbr, chapter, startVerse, endVerse);
        return ResponseEntity.ok(verseDtoList);
    }

    @GetMapping("/books/{abbr}/chapters/{chapter}/verses/{verse}")
    public ResponseEntity<VerseResponse> getVerse(
            @PathVariable String abbr,
            @PathVariable Short chapter,
            @PathVariable Short verse) {
        log.info("GET /bible/books/{}/chapters/{}/verses/{}", abbr, chapter, verse);
        VerseResponse verseDto = bibleService.readVerse(abbr, chapter, verse);
        return ResponseEntity.ok(verseDto);
    }
}
