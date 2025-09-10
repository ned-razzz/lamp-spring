package ch.hambak.lamp.bible;

import ch.hambak.lamp.bible.dto.BookResponse;
import ch.hambak.lamp.bible.dto.VerseResponse;
import ch.hambak.lamp.bible.service.BibleApplicationService;
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

    private final BibleApplicationService bibleService;

    @GetMapping("/books/{abbr}")
    public ResponseEntity<BookResponse> getBook(@PathVariable String abbr) {
        log.info("GET /bible/books/{}", abbr);
        BookResponse bookDto = bibleService.readBook(abbr);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/books/{abbr}/chapters/{chapter}/verses/range")
    public List<VerseResponse> getVerses(
            @PathVariable String abbr,
            @PathVariable Short chapter,
            @RequestParam Short startVerse,
            @RequestParam Short endVerse) {
        log.info("GET /bible/books/{}/chapters/{}?startVerse={}&endVerse={}",abbr, chapter, startVerse, endVerse);
        return bibleService.readVersesRange(abbr, chapter, startVerse, endVerse);
    }

    @GetMapping("/books/{abbr}/chapters/{chapter}/verses/{verse}")
    public VerseResponse getVerse(
            @PathVariable String abbr,
            @PathVariable Short chapter,
            @PathVariable Short verse) {
        log.info("GET /bible/books/{}/chapters/{}/verses/{}", abbr, chapter, verse);
        return bibleService.readVerse(abbr, chapter, verse);
    }
}
