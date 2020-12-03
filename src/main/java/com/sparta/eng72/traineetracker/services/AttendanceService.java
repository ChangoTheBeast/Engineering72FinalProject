package com.sparta.eng72.traineetracker.services;

import com.sparta.eng72.traineetracker.entities.TraineeAttendance;
import com.sparta.eng72.traineetracker.repositories.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                return "--";
        }
    }

}
