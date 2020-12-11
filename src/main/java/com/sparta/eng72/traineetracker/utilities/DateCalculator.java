package com.sparta.eng72.traineetracker.utilities;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

public class DateCalculator {

    public static int getTotalDaysSinceDate(Date attendanceDate, Date startDate){
        return (int)TimeUnit.DAYS.convert(Math.abs(attendanceDate.getTime() - startDate.getTime()), TimeUnit.MILLISECONDS);
    }

    public static int getWeek(Date attendanceDate, Date startDate){
        return (getTotalDaysSinceDate(attendanceDate, startDate) / 7) + 1;
    }

    public static int getDay(Date attendanceDate, Date startDate) {
        return (getTotalDaysSinceDate(attendanceDate, startDate) % 7) + 1;
    }
}
