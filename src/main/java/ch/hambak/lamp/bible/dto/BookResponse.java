package ch.hambak.lamp.bible.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 5)
    private String abbrKor;

    @NotBlank
    @Size(max = 5)
    private String abbrEng;

    @NotNull
    private Short bookOrder;
}
