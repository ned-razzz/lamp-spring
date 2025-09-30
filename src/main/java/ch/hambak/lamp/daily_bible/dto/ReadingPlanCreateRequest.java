package ch.hambak.lamp.daily_bible.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingPlanCreateRequest {

    @NotNull
    private BibleIndex bibleIndex;

    @NotNull
    @Positive
    private Integer amountPerDay;

    @NotNull
    @Positive
    private Integer threshold;
}
