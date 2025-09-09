package ch.hambak.lamp.daily_bible.entity;

import ch.hambak.lamp.bible.entity.Verse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalReadingPlan {

    @Id
    @Column(name = "global_reading_plan_id")
    private Long id;

    @Column(nullable = false)
    private Short amountPerDay;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verse_id")
    private Verse verse;

    //== Business Logic ==//
    public void update(Verse verse, Short amountPerDay) {
        this.verse = verse;
        //todo: Assert 구문으로 대체하기
        if (amountPerDay != null) {
            this.amountPerDay = amountPerDay;
        }
    }
}
