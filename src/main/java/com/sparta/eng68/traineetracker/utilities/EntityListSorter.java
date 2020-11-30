package com.sparta.eng68.traineetracker.utilities;

import com.sparta.eng68.traineetracker.entities.WeekReport;

import java.util.ArrayList;
import java.util.List;

public class EntityListSorter {

    public static List<WeekReport> sortWeekReportListForProgress(List<WeekReport> weekReportList, Integer courseDuration) {

        List<WeekReport> sortedList = new ArrayList<>();

        for (WeekReport weekReport : weekReportList) {
            sortedList.add(weekReport.getWeekNum()-1, weekReport);
        }

        return sortedList;
    }

}
