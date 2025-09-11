package ch.hambak.lamp.daily_bible.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingPlanUpdateRequest {

    @NotBlank
    @Size(max = 5)
    private String bookAbbr;

    @NotNull
    private Integer chapterOrdinal;

    @NotNull
    private Integer verseOrdinal;

    @Min(1)
    private Integer countPerDay;

    @Min(1)
    private Integer versesLeftThreshold;
}
