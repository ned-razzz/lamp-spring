package ch.hambak.lamp.daily_bible.entity;

import ch.hambak.lamp.bible.entity.Verse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Entity
@Check(constraints = "count_per_day >= 1")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalReadingPlan {

    @Id
    @Column(name = "global_reading_plan_id")
    private Long id;

    @Column(nullable = false)
    private Integer countPerDay;

    @Column(nullable = false)
    private Integer versesLeftThreshold;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_verse_id")
    private Verse startVerse;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_verse_id")
    private Verse endVerse;

    //== Business Logic ==//
    public void update(Verse startVerse, Verse endVerse, Integer countPerDay, Integer chapterTailThreshold) {
        this.startVerse = startVerse;
        this.endVerse = endVerse;

        //todo: Assert 구문으로 대체하기
        if (countPerDay != null) {
            this.countPerDay = countPerDay;
        }

        if (chapterTailThreshold != null) {
            this.versesLeftThreshold = chapterTailThreshold;
        }
    }
}
