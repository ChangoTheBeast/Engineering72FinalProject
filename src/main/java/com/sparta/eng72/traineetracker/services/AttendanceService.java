package com.sparta.eng72.traineetracker.services;


import com.sparta.eng72.traineetracker.entities.Trainee;
import com.sparta.eng72.traineetracker.entities.TraineeAttendance;
import com.sparta.eng72.traineetracker.repositories.AttendanceRepository;
import com.sparta.eng72.traineetracker.utilities.DateCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<TraineeAttendance> getTraineeAttendanceByTraineeId(Integer traineeId){
        return attendanceRepository.getTraineeAttendanceByTraineeId(traineeId);
    }

    public List<TraineeAttendance> getTraineeAttendanceByTraineeIdAndWeek(Integer traineeId, Integer week){
        return attendanceRepository.getTraineeAttendanceByTraineeIdAndWeek(traineeId, week);
    }

    public String getAttendanceStatus(Integer attendanceId) {
        switch (attendanceId) {
            case 1:
                return "ON TIME";
            case 2:
                return "LATE";
            case 3:
                return "ABSENT (EXCUSED)";
            case 4:
                return "ABSENT (UNEXCUSED)";
            default:
                return "NO ENTRY";
        }
    }

    public void saveAllAttendances(List<TraineeAttendance> traineeAttendances){
        attendanceRepository.saveAll(traineeAttendances);
    }

    public void saveAttendance(TraineeAttendance traineeAttendance){
        attendanceRepository.save(traineeAttendance);
    }

    private Calendar getEndCalendar(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        return calendar;
    }

    private Calendar getStartCalendar(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    private TraineeAttendance setDefaultAttendance(Trainee trainee, Date startDate, Date date) {
        TraineeAttendance traineeAttendance = new TraineeAttendance();
        traineeAttendance.setAttendanceId(5);
        traineeAttendance.setAttendanceDate(date);
        traineeAttendance.setTraineeId(trainee.getTraineeId());
        traineeAttendance.setDay(DateCalculator.getDay(date, startDate));
        traineeAttendance.setWeek(DateCalculator.getWeek(date, startDate));
        return traineeAttendance;
    }

    public void setInitialTraineeAttendance(Trainee trainee, Date startDate, Date endDate){

        Calendar calendar = getStartCalendar(startDate);
        Calendar endCalendar = getEndCalendar(endDate);

        List<TraineeAttendance> traineeAttendances = new ArrayList();
        while (calendar.before(endCalendar)) {
            Date date = new java.sql.Date(calendar.getTimeInMillis());
            if (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7 ){
                calendar.add(Calendar.DATE, 1);
                continue;
            }
            TraineeAttendance traineeAttendance = setDefaultAttendance(trainee, startDate, date);
            traineeAttendances.add(traineeAttendance);
            calendar.add(Calendar.DATE, 1);
        }
        saveAllAttendances(traineeAttendances);
    }

}
