package com.sparta.eng72.traineetracker.entities;

import java.sql.Date;

public class AttendanceReport {

    private String attendanceStatus;
    private Date attendanceDate;
    private Integer day;
    private Integer week;

    public AttendanceReport(String attendanceStatus, Date attendanceDate, Integer day, Integer week) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDate = attendanceDate;
        this.day = day;
        this.week = week;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }



}
