package ch.hambak.lamp.bible.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private Long id;

    private String name;

    private String abbrKor;

    private String abbrEng;

    private Integer sequence;
}
