package ch.hambak.lamp.daily_bible.service;

import ch.hambak.lamp.daily_bible.dto.ReadingPlanUpdateRequest;
import ch.hambak.lamp.daily_bible.dto.TodayBibleResponse;

public interface DailyBibleApplicationService {

    /**
     * 오늘 읽어야 할 성경 구절을 조회합니다.
     * - 현재 챕터에 남은 절이 9절 이하이면 남은 절 전체를 반환합니다.
     * - 그렇지 않으면 관리자가 설정한 하루 분량(versesPerDay)만큼 반환합니다.
     * @return 오늘의 성경 구절 정보를 담은 DTO
     */
    TodayBibleResponse readTodayVerses();

    /**
     * 읽기 진도를 다음 날로 업데이트합니다.
     * - getTodaysReading() 로직에 따라 오늘 읽은 분량을 계산합니다.
     * - 마지막으로 읽은 구절을 기준으로 다음 시작점을 찾습니다.
     * - 절 -> 장 -> 책 순으로 넘어가며, 성경 전체를 다 읽으면 창세기로 순환합니다.
     * - 이 메소드는 스케줄러에 의해 매일 자정에 호출되어야 합니다.
     */
    void advanceToNextDay();

    /**
     * 관리자가 전체 읽기 진도의 현재 위치와 하루 분량을 강제로 설정합니다.
     * @param updateRequest 관리자가 설정할 책, 장, 절, 하루 분량 정보를 담은 DTO
     */
    void updatePlan(ReadingPlanUpdateRequest updateRequest);
}