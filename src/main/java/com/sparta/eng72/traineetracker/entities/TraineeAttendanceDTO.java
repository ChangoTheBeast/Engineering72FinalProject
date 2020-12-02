package com.sparta.eng72.traineetracker.entities;

import java.sql.Date;
import java.util.List;

public class TraineeAttendanceDTO {
    private List<TraineeAttendance> traineeAttendanceList;
    private Date date;

    public void addTraineeAttendance(TraineeAttendance traineeAttendance) {
        this.traineeAttendanceList.add(traineeAttendance);
    }

    public List<TraineeAttendance> getTraineeAttendanceList() {
        return traineeAttendanceList;
    }

    public void setTraineeAttendanceList(List<TraineeAttendance> traineeAttendanceList) {
        this.traineeAttendanceList = traineeAttendanceList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
