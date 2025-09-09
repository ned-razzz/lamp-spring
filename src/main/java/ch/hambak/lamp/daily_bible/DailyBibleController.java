package ch.hambak.lamp.daily_bible;

import ch.hambak.lamp.daily_bible.dto.TodayBibleResponse;
import ch.hambak.lamp.daily_bible.dto.ReadingPlanUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/daily-bible")
@Slf4j
@RequiredArgsConstructor
public class DailyBibleController {
    private final DailyBibleService dailyBibleService;

    @GetMapping("/today")
    public TodayBibleResponse getDailyBible() {
        log.info("/api/daily-bible/today");
        return dailyBibleService.readTodayVerses();
    }

    @PatchMapping("/admin/update")
    public void patchReadingPlan(@RequestBody ReadingPlanUpdateRequest updateRequest) {
        log.info("/api/daily-bible/admin/update");
        dailyBibleService.updatePlan(updateRequest);
    }

    @PostMapping("/admin/next-day")
    public void advanceDailyBible() {
        log.info("/api/daily-bible/admin/next-day");
        dailyBibleService.advanceToNextDay();
    }
}
