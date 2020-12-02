package com.sparta.eng72.traineetracker.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "trainee_assessments", schema = "training_tracker")
public class TraineeAssessments implements Serializable {
    private Integer traineeId;
    private Integer assessmentId;

    @Id
    @Column(name = "trainee_id")
    public Integer getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
    }

    @Id
    @Column(name = "assessment_id")
    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraineeAssessments that = (TraineeAssessments) o;
        return Objects.equals(traineeId, that.traineeId) &&
                Objects.equals(assessmentId, that.assessmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traineeId, assessmentId);
    }
}
