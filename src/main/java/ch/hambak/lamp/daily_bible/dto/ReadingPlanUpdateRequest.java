package ch.hambak.lamp.daily_bible.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingPlanUpdateRequest {

    @Size(max = 5)
    private String bookAbbr;

    @Positive
    private Integer chapterOrdinal;

    @Positive
    private Integer verseOrdinal;

    @Positive
    private Integer amountPerDay;

    @Positive
    private Integer threshold;
}
