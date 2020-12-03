package com.sparta.eng72.traineetracker.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class TraineeAttendancePK implements Serializable {
    private Integer traineeId;
    private Date attendanceDate;

    @Column(name = "trainee_id")
    @Id
    public Integer getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
    }

    @Column(name = "attendance_date")
    @Id
    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeAttendancePK that = (TraineeAttendancePK) o;
        return Objects.equals(traineeId, that.traineeId) &&
                Objects.equals(attendanceDate, that.attendanceDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, attendanceDate);
    }
}
