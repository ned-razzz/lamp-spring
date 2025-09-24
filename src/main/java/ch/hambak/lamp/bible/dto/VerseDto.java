package ch.hambak.lamp.bible.dto;

import ch.hambak.lamp.bible.entity.Chapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerseDto {
    private Long id;

    private Integer ordinal;

    private String text;
}
