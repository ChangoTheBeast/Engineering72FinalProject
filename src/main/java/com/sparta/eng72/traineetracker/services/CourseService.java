package com.sparta.eng72.traineetracker.services;

import com.sparta.eng72.traineetracker.entities.Course;
import com.sparta.eng72.traineetracker.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Optional<Course> getGroupByID(Integer id) {
        return (Optional<Course>) courseRepository.findById(id);
    }

    public Optional<Course> getCourseByID(Integer id) {
        return (Optional<Course>) courseRepository.findById(id);
    }

    public List<Course> getAllCourses() {
        return (List<Course>) courseRepository.findAll();
    }



}
