package com.sparta.eng72.traineetracker.entities;

import javax.persistence.*;

@Entity
public class Trainee {
    private Integer traineeId;
    private Integer groupId;
    private String firstName;
    private String lastName;
    private String username;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainee_id")
    public Integer getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(Integer traineeId) {
        this.traineeId = traineeId;
    }

    @Basic
    @Column(name = "group_id")
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trainee trainee = (Trainee) o;

        if (traineeId != null ? !traineeId.equals(trainee.traineeId) : trainee.traineeId != null) return false;
        if (groupId != null ? !groupId.equals(trainee.groupId) : trainee.groupId != null) return false;
        if (firstName != null ? !firstName.equals(trainee.firstName) : trainee.firstName != null) return false;
        if (lastName != null ? !lastName.equals(trainee.lastName) : trainee.lastName != null) return false;
        if (username != null ? !username.equals(trainee.username) : trainee.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = traineeId != null ? traineeId.hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
