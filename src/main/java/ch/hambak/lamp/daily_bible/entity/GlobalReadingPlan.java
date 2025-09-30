package ch.hambak.lamp.daily_bible.entity;

import ch.hambak.lamp.bible.entity.Verse;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Entity
@Check(constraints = "amount_per_day >= 1")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalReadingPlan {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "global_reading_plan_id")
    private Long id;

    @Column(nullable = false)
    private Integer amountPerDay;

    @Column(nullable = false)
    private Integer threshold;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_verse_id")
    private Verse startVerse;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_verse_id")
    private Verse endVerse;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReadingPlanStatus status;

    @Column
    @PastOrPresent
    private LocalDateTime created;

    @Column
    @PastOrPresent
    private LocalDateTime updated;

    //== Business Logic ==//
    public static GlobalReadingPlan create(Verse startVerse, Verse endVerse, int amountPerDay, int threshold) {
        GlobalReadingPlan readingPlan = new GlobalReadingPlan();
        readingPlan.id = 1L;
        readingPlan.startVerse = startVerse;
        readingPlan.endVerse = endVerse;
        readingPlan.amountPerDay = amountPerDay;
        readingPlan.threshold = threshold;
        readingPlan.status = ReadingPlanStatus.ACTIVE;
        readingPlan.created = LocalDateTime.now();
        readingPlan.updated = LocalDateTime.now();
        return readingPlan;
    }

    public void updateRange(Verse startVerse, Verse endVerse) {
        this.startVerse = startVerse;
        this.endVerse = endVerse;
        this.updated = LocalDateTime.now();
    }

    public void updateDetails(Integer amountPerDay, Integer threshold) {
        if (amountPerDay != null) {
            this.amountPerDay = amountPerDay;
        }
        if (threshold != null) {
            this.threshold = threshold;
        }
        this.updated = LocalDateTime.now();
    }

    public void delete() {
        this.status = ReadingPlanStatus.DELETED;
        this.updated = LocalDateTime.now();
    }
}
