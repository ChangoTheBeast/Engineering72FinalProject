package com.sparta.eng68.traineetracker.repositories;

import com.sparta.eng68.traineetracker.entities.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

}
