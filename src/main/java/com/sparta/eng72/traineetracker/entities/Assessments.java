package com.sparta.eng72.traineetracker.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Assessments {
    private Integer assessmentId;
    private Integer testId;

    @Id
    @Column(name = "assessment_id")
    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    @Basic
    @Column(name = "test_id")
    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assessments that = (Assessments) o;
        return Objects.equals(assessmentId, this.assessmentId) &&
                Objects.equals(testId, that.testId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentId, testId);
    }
}
