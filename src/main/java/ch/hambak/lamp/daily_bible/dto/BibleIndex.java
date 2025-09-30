package ch.hambak.lamp.daily_bible.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BibleIndex {
    @NotBlank
    @Size(max = 5)
    private String bookAbbr;

    @NotNull
    @Positive
    private Integer chapterOrdinal;

    @NotNull
    @Positive
    private Integer verseOrdinal;
}
