package ch.hambak.lamp.daily_bible;

import ch.hambak.lamp.daily_bible.dto.TodayBibleResponse;
import ch.hambak.lamp.daily_bible.dto.ReadingPlanUpdateRequest;
import ch.hambak.lamp.daily_bible.service.DailyBibleApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/daily-bible")
@Slf4j
@RequiredArgsConstructor
public class DailyBibleController {
    private final DailyBibleApplicationService dailyBibleService;

    @GetMapping("/today")
    public TodayBibleResponse getDailyBible() {
        return dailyBibleService.readTodayVerses();
    }

    @PatchMapping("/admin/update")
    public void patchReadingPlan(@RequestBody ReadingPlanUpdateRequest updateRequest) {
        dailyBibleService.updatePlan(updateRequest);
    }

    @PostMapping("/admin/next-day")
    public void advanceDailyBible() {
        dailyBibleService.advanceToNextDay();
    }
}
