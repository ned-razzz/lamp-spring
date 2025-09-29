package ch.hambak.lamp.bible;

import ch.hambak.lamp.bible.dto.BookResponse;
import ch.hambak.lamp.bible.dto.VerseResponse;
import ch.hambak.lamp.bible.dto.VersesRangeParam;
import ch.hambak.lamp.bible.service.BibleApplicationService;
import jakarta.validation.Valid;
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
        BookResponse bookDto = bibleService.readBook(abbr);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/books/{abbr}/chapters/{chapter}/verses/range")
    public List<VerseResponse> getVerses(
            @PathVariable String abbr,
            @PathVariable int chapter,
            @ModelAttribute @Valid VersesRangeParam rangeParam) {
        if (rangeParam.getStartVerse() > rangeParam.getEndVerse()) {
            throw new IllegalArgumentException("Invalid Verse Range");
        }
        return bibleService.readVersesRange(abbr, chapter, rangeParam.getStartVerse(), rangeParam.getEndVerse());
    }

    @GetMapping("/books/{abbr}/chapters/{chapter}/verses/{verse}")
    public VerseResponse getVerse(
            @PathVariable String abbr,
            @PathVariable int chapter,
            @PathVariable int verse) {
        return bibleService.readVerse(abbr, chapter, verse);
    }
}
