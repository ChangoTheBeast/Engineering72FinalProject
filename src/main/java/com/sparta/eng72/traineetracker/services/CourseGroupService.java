package com.sparta.eng72.traineetracker.services;

import com.sparta.eng72.traineetracker.entities.CourseGroup;
import com.sparta.eng72.traineetracker.repositories.CourseGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseGroupService {
    private final CourseGroupRepository courseGroupRepository;

    @Autowired
    public CourseGroupService(CourseGroupRepository courseGroupRepository) {
        this.courseGroupRepository = courseGroupRepository;
    }

    public Optional<CourseGroup> getGroupByID(Integer id) {
        return (Optional<CourseGroup>) courseGroupRepository.findById(id);
    }


    public List<CourseGroup> getAllCourseGroups() {
        return (List<CourseGroup>) courseGroupRepository.findAll();
    }

    public void incrementWeek(int id){
         Optional<CourseGroup> courseGroup =courseGroupRepository.findById(id);
         CourseGroup courseGroup1 = courseGroup.get();
         int week_num = courseGroup1.getCurrentWeek();
         week_num++;
         courseGroup1.setCurrentWeek(week_num);
         courseGroupRepository.save(courseGroup1);
    }

    public int getWeekByGroupId(int id) {
        Optional<CourseGroup> courseGroup = courseGroupRepository.findById(id);
        if (courseGroup.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        CourseGroup courseGroup1 = courseGroup.get();
        int week_num = courseGroup1.getCurrentWeek();
        return week_num;
    }

    public boolean saveNewGroup(CourseGroup newGroup) {
        if (groupExists(newGroup) == false) {
            courseGroupRepository.save(newGroup);
            return true;
        }
        else {
            return false;
        }
    }


    public boolean groupExists(CourseGroup newGroup){
        //check database
        //if group already exists return true
        if (courseGroupRepository.findCourseGroupByGroupName(newGroup.getGroupName()) == null){
            return false;
        }
        return true;
    }


}
