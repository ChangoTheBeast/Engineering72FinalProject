package com.sparta.eng72.traineetracker.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Attendance {
    private Integer attendanceId;
    private String attendanceStatus;

    @Id
    @Column(name = "attendance_id")
    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    @Basic
    @Column(name = "attendance_status")
    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(attendanceId, that.attendanceId) &&
                Objects.equals(attendanceStatus, that.attendanceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendanceId, attendanceStatus);
    }

}
