package ch.hambak.lamp.daily_bible;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalProgress {

    @Id
    @Column(name = "global_progress_id")
    private Long id;

    @Column
    private Short amountPerDay;

    @Column
    private Short currentVerseId;
}
