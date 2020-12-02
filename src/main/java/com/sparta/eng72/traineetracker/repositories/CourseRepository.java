package com.sparta.eng72.traineetracker.repositories;

import com.sparta.eng72.traineetracker.entities.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

}
