package ch.hambak.lamp.daily_bible.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingPlanUpdateRequest {

    private BibleIndex bibleIndex;

    @Positive
    private Integer amountPerDay;

    @Positive
    private Integer threshold;
}
