package ch.hambak.lamp.daily_bible.repository;

import ch.hambak.lamp.daily_bible.entity.GlobalReadingPlan;

import java.util.Optional;

public interface ReadingPlanRepository {
    Long save(GlobalReadingPlan readingPlan);

    Optional<GlobalReadingPlan> find();
}
