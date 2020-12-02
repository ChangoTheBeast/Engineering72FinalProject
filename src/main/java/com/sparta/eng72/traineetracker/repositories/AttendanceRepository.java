package com.sparta.eng72.traineetracker.repositories;

import com.sparta.eng72.traineetracker.entities.TraineeAttendance;
import com.sparta.eng72.traineetracker.entities.TraineeAttendancePK;
import org.springframework.data.repository.CrudRepository;

public interface AttendanceRepository extends CrudRepository<TraineeAttendance, TraineeAttendancePK> {
}
