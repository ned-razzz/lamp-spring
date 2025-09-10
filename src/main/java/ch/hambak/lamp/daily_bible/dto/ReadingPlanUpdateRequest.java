package ch.hambak.lamp.daily_bible.dto;

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
    String bookAbbr;

    //todo: 변수명에 ordinal 추가하기
    @NotNull
    Integer chapter;

    @NotNull
    Integer verse;

    Integer countPerDay;
}
