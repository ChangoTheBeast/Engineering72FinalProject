package com.sparta.eng68.traineetracker.repositories;

import com.sparta.eng68.traineetracker.entities.WeekReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface WeekReportRepository extends CrudRepository<WeekReport, Integer> {

    List<WeekReport> findByTraineeIdOrderByWeekNumAsc(Integer traineeId);

    Optional<WeekReport> findByWeekNumAndTraineeId(Integer weekNum, Integer traineeId);

    Optional<WeekReport> findFirstByWeekNumAndTraineeId(Integer weekNum, Integer traineeId);

    void deleteByTraineeId(Integer traineeId);

}
