package ch.hambak.lamp.daily_bible.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayBibleResponse {
    @NotBlank
    @Size(max = 20)
    private String bookName;

    @NotNull
    private Integer chapterOrdinal;

    @NotEmpty
    private List<DailyVerseResponse> verses;
}
