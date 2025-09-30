package ch.hambak.lamp.daily_bible;

import ch.hambak.lamp.daily_bible.dto.ReadingPlanCreateRequest;
import ch.hambak.lamp.daily_bible.dto.TodayBibleResponse;
import ch.hambak.lamp.daily_bible.dto.ReadingPlanUpdateRequest;
import ch.hambak.lamp.daily_bible.service.DailyBibleApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/daily-bible")
@Slf4j
@RequiredArgsConstructor
public class DailyBibleController {
    private final DailyBibleApplicationService dailyBibleService;

    @GetMapping("/today")
    public TodayBibleResponse getDailyBible() {
        return dailyBibleService.readTodayVerses();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin")
    public void createReadingPlan(@RequestBody @Valid ReadingPlanCreateRequest createRequest) {
        dailyBibleService.createPlan(createRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/admin")
    public void updateReadingPlan(@RequestBody @Valid ReadingPlanUpdateRequest updateRequest) {
        dailyBibleService.updatePlan(updateRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/admin/next-day")
    public void advanceDailyBible() {
        dailyBibleService.advanceToNextDay();
    }
}
