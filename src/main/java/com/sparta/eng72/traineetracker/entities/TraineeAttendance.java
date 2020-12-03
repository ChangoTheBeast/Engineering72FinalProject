package com.sparta.eng72.traineetracker.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "trainee_attendance", schema = "training_tracker", catalog = "")
@IdClass(TraineeAttendancePK.class)
public class TraineeAttendance implements Serializable {
    private Integer traineeId;
    private Date attendanceDate;
    private Integer attendanceId;

    @Id
    @Column(name = "trainee_id")
    public Integer getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
    }

    @Id
    @Column(name = "attendance_date")
    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    @Basic
    @Column(name = "attendance_id")
    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeAttendance that = (TraineeAttendance) o;
        return Objects.equals(traineeId, that.traineeId) &&
                Objects.equals(attendanceDate, that.attendanceDate) &&
                Objects.equals(attendanceId, that.attendanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, attendanceDate, attendanceId);
    }

}
