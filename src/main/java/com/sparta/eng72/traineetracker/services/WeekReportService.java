package com.sparta.eng72.traineetracker.services;

import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.WeekReport;
import com.sparta.eng72.traineetracker.repositories.CourseGroupRepository;
import com.sparta.eng72.traineetracker.repositories.TraineeRepository;
import com.sparta.eng72.traineetracker.repositories.WeekReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class WeekReportService {
    private final WeekReportRepository weekReportRepository;
    private final CourseGroupRepository courseGroupRepository;
    private final TraineeRepository traineeRepository;

    @Autowired
    public WeekReportService(WeekReportRepository weekReportRepository, CourseGroupRepository courseGroupRepository,
                             TraineeRepository traineeRepository) {
        this.weekReportRepository = weekReportRepository;
        this.courseGroupRepository = courseGroupRepository;
        this.traineeRepository = traineeRepository;
    }

    public Optional<WeekReport> getWeekReportByReportId(Integer reportId) {
        return weekReportRepository.findById(reportId);
    }

    public List<WeekReport> getAllReports() {
        return (List<WeekReport>) weekReportRepository.findAll();
    }

    public Optional<WeekReport> getReportByID(Integer id) {
        return (Optional<WeekReport>) weekReportRepository.findById(id);
    }


    public List<WeekReport> getReportsByTraineeID(Integer traineeId) {
        return weekReportRepository.findByTraineeIdOrderByWeekNumAsc(traineeId);
    }

    public void createReports(List<WeekReport> weekReports) {
        for (WeekReport weekReport : weekReports) {
            weekReportRepository.save(weekReport);
        }
    }

    public Optional<WeekReport> getCurrentWeekReportByTraineeID(Integer traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId).get();
        Integer currentWeek = courseGroupRepository.findFirstByGroupIdOrderByCurrentWeekDesc(trainee.getGroupId()).get().getCurrentWeek();
        Optional<WeekReport> weekReport = weekReportRepository.findFirstByWeekNumAndTraineeId(currentWeek, traineeId);

        return weekReport;
    }

    public Optional<WeekReport> getWeekReportByTraineeIdAndWeekNum(Integer traineeId, Integer weekNum) {
        return weekReportRepository.findByWeekNumAndTraineeId(weekNum, traineeId);
    }

    public void updateWeekReport(WeekReport weekReport) {
        weekReportRepository.save(weekReport);
    }

    public Optional<WeekReport> getPreviousWeekReportByTraineeID(Integer traineeId){
        Trainee trainee = traineeRepository.findById(traineeId).get();
        Integer currentWeek = courseGroupRepository.findFirstByGroupIdOrderByCurrentWeekDesc(trainee.getGroupId()).get().getCurrentWeek();
        if (currentWeek < 2){
            Optional<WeekReport> weekReport = Optional.empty();
            return weekReport;
        }
        return weekReportRepository.findByWeekNumAndTraineeId(currentWeek - 1,traineeId);
    }

    public void deleteReportsByTraineeID(Integer traineeId) {
        weekReportRepository.deleteByTraineeId(traineeId);

    }
}
