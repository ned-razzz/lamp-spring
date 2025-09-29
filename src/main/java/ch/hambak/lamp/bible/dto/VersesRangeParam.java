package ch.hambak.lamp.bible.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersesRangeParam {
    @NotNull
    @Positive
    private int startVerse;

    @NotNull
    @Positive
    private int endVerse;
}
