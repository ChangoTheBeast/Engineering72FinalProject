package com.sparta.eng68.traineetracker.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "week_report", schema = "training_tracker", catalog = "")
public class WeekReport {
    private Integer reportId;
    private Integer traineeId;
    private Integer weekNum;
    private LocalDateTime deadline;
    private String consultantGradeTrainee;
    private String technicalGradeTrainee;
    private String startTrainee;
    private String stopTrainee;
    private String continueTrainee;
    private String consultantGradeTrainer;
    private String technicalGradeTrainer;
    private String overallGradeTrainer;
    private String trainerComments;
    private String startTrainer;
    private String stopTrainer;
    private String continueTrainer;
    private LocalDateTime mostRecentEdit;
    private Byte traineeStartFlag;
    private Byte traineeStopFlag;
    private Byte traineeContinueFlag;
    private Byte traineeConsultantGradeFlag;
    private Byte traineeTechnicalGradeFlag;
    private Byte traineeSubmittedFlag;
    private Byte trainerCompletedFlag;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    @Basic
    @Column(name = "trainee_id")
    public Integer getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
    }

    @Basic
    @Column(name = "week_num")
    public Integer getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(Integer weekNum) {
        this.weekNum = weekNum;
    }

    @Basic
    @Column(name = "deadline")
    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    @Basic
    @Column(name = "consultant_grade_trainee")
    public String getConsultantGradeTrainee() {
        return consultantGradeTrainee;
    }

    public void setConsultantGradeTrainee(String consultantGradeTrainee) {
        this.consultantGradeTrainee = consultantGradeTrainee;
    }

    @Basic
    @Column(name = "technical_grade_trainee")
    public String getTechnicalGradeTrainee() {
        return technicalGradeTrainee;
    }

    public void setTechnicalGradeTrainee(String technicalGradeTrainee) {
        this.technicalGradeTrainee = technicalGradeTrainee;
    }

    @Basic
    @Column(name = "start_trainee")
    public String getStartTrainee() {
        return startTrainee;
    }

    public void setStartTrainee(String startTrainee) {
        this.startTrainee = startTrainee;
    }

    @Basic
    @Column(name = "stop_trainee")
    public String getStopTrainee() {
        return stopTrainee;
    }

    public void setStopTrainee(String stopTrainee) {
        this.stopTrainee = stopTrainee;
    }

    @Basic
    @Column(name = "continue_trainee")
    public String getContinueTrainee() {
        return continueTrainee;
    }

    public void setContinueTrainee(String continueTrainee) {
        this.continueTrainee = continueTrainee;
    }

    @Basic
    @Column(name = "consultant_grade_trainer")
    public String getConsultantGradeTrainer() {
        return consultantGradeTrainer;
    }

    public void setConsultantGradeTrainer(String consultantGradeTrainer) {
        this.consultantGradeTrainer = consultantGradeTrainer;
    }

    @Basic
    @Column(name = "technical_grade_trainer")
    public String getTechnicalGradeTrainer() {
        return technicalGradeTrainer;
    }

    public void setTechnicalGradeTrainer(String technicalGradeTrainer) {
        this.technicalGradeTrainer = technicalGradeTrainer;
    }

    @Basic
    @Column(name = "overall_grade_trainer")
    public String getOverallGradeTrainer() {
        return overallGradeTrainer;
    }

    public void setOverallGradeTrainer(String overallGradeTrainer) {
        this.overallGradeTrainer = overallGradeTrainer;
    }

    @Basic
    @Column(name = "trainer_comments")
    public String getTrainerComments() {
        return trainerComments;
    }

    public void setTrainerComments(String trainerComments) {
        this.trainerComments = trainerComments;
    }

    @Basic
    @Column(name = "start_trainer")
    public String getStartTrainer() {
        return startTrainer;
    }

    public void setStartTrainer(String startTrainer) {
        this.startTrainer = startTrainer;
    }

    @Basic
    @Column(name = "stop_trainer")
    public String getStopTrainer() {
        return stopTrainer;
    }

    public void setStopTrainer(String stopTrainer) {
        this.stopTrainer = stopTrainer;
    }

    @Basic
    @Column(name = "continue_trainer")
    public String getContinueTrainer() {
        return continueTrainer;
    }

    public void setContinueTrainer(String continueTrainer) {
        this.continueTrainer = continueTrainer;
    }

    @Basic
    @Column(name = "most_recent_edit")
    public LocalDateTime getMostRecentEdit() {
        return mostRecentEdit;
    }

    public void setMostRecentEdit(LocalDateTime mostRecentEdit) {
        this.mostRecentEdit = mostRecentEdit;
    }

    @Basic
    @Column(name = "trainee_start_flag")
    public Byte getTraineeStartFlag() {
        return traineeStartFlag;
    }

    public void setTraineeStartFlag(Byte traineeStartFlag) {
        this.traineeStartFlag = traineeStartFlag;
    }

    @Basic
    @Column(name = "trainee_stop_flag")
    public Byte getTraineeStopFlag() {
        return traineeStopFlag;
    }

    public void setTraineeStopFlag(Byte traineeStopFlag) {
        this.traineeStopFlag = traineeStopFlag;
    }

    @Basic
    @Column(name = "trainee_continue_flag")
    public Byte getTraineeContinueFlag() {
        return traineeContinueFlag;
    }

    public void setTraineeContinueFlag(Byte traineeContinueFlag) {
        this.traineeContinueFlag = traineeContinueFlag;
    }

    @Basic
    @Column(name = "trainee_consultant_grade_flag")
    public Byte getTraineeConsultantGradeFlag() {
        return traineeConsultantGradeFlag;
    }

    public void setTraineeConsultantGradeFlag(Byte traineeConsultantGradeFlag) {
        this.traineeConsultantGradeFlag = traineeConsultantGradeFlag;
    }

    @Basic
    @Column(name = "trainee_technical_grade_flag")
    public Byte getTraineeTechnicalGradeFlag() {
        return traineeTechnicalGradeFlag;
    }

    public void setTraineeTechnicalGradeFlag(Byte traineeTechnicalGradeFlag) {
        this.traineeTechnicalGradeFlag = traineeTechnicalGradeFlag;
    }

    @Basic
    @Column(name = "trainee_submitted_flag")
    public Byte getTraineeSubmittedFlag() {
        return traineeSubmittedFlag;
    }

    public void setTraineeSubmittedFlag(Byte traineeSubmittedFlag) {
        this.traineeSubmittedFlag = traineeSubmittedFlag;
    }

    @Basic
    @Column(name = "trainer_completed_flag")
    public Byte getTrainerCompletedFlag() {
        return trainerCompletedFlag;
    }

    public void setTrainerCompletedFlag(Byte trainerCompletedFlag) {
        this.trainerCompletedFlag = trainerCompletedFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekReport that = (WeekReport) o;
        return Objects.equals(reportId, that.reportId) &&
                Objects.equals(traineeId, that.traineeId) &&
                Objects.equals(weekNum, that.weekNum) &&
                Objects.equals(deadline, that.deadline) &&
                Objects.equals(consultantGradeTrainee, that.consultantGradeTrainee) &&
                Objects.equals(technicalGradeTrainee, that.technicalGradeTrainee) &&
                Objects.equals(startTrainee, that.startTrainee) &&
                Objects.equals(stopTrainee, that.stopTrainee) &&
                Objects.equals(continueTrainee, that.continueTrainee) &&
                Objects.equals(consultantGradeTrainer, that.consultantGradeTrainer) &&
                Objects.equals(technicalGradeTrainer, that.technicalGradeTrainer) &&
                Objects.equals(overallGradeTrainer, that.overallGradeTrainer) &&
                Objects.equals(trainerComments, that.trainerComments) &&
                Objects.equals(startTrainer, that.startTrainer) &&
                Objects.equals(stopTrainer, that.stopTrainer) &&
                Objects.equals(continueTrainer, that.continueTrainer) &&
                Objects.equals(mostRecentEdit, that.mostRecentEdit) &&
                Objects.equals(traineeStartFlag, that.traineeStartFlag) &&
                Objects.equals(traineeStopFlag, that.traineeStopFlag) &&
                Objects.equals(traineeContinueFlag, that.traineeContinueFlag) &&
                Objects.equals(traineeConsultantGradeFlag, that.traineeConsultantGradeFlag) &&
                Objects.equals(traineeTechnicalGradeFlag, that.traineeTechnicalGradeFlag) &&
                Objects.equals(traineeSubmittedFlag, that.traineeSubmittedFlag) &&
                Objects.equals(trainerCompletedFlag, that.trainerCompletedFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, traineeId, weekNum, deadline, consultantGradeTrainee, technicalGradeTrainee, startTrainee, stopTrainee, continueTrainee, consultantGradeTrainer, technicalGradeTrainer, overallGradeTrainer, trainerComments, startTrainer, stopTrainer, continueTrainer, mostRecentEdit, traineeStartFlag, traineeStopFlag, traineeContinueFlag, traineeConsultantGradeFlag, traineeTechnicalGradeFlag, traineeSubmittedFlag, trainerCompletedFlag);
    }
}
