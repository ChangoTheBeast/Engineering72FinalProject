package com.sparta.eng68.traineetracker.entities;

import javax.persistence.*;

@Entity
public class Trainer {
    private Integer trainerId;
    private Integer groupId;
    private String firstName;
    private String lastName;
    private String username;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "trainer_id")
    public Integer getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
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

        Trainer trainer = (Trainer) o;

        if (trainerId != null ? !trainerId.equals(trainer.trainerId) : trainer.trainerId != null) return false;
        if (groupId != null ? !groupId.equals(trainer.groupId) : trainer.groupId != null) return false;
        if (firstName != null ? !firstName.equals(trainer.firstName) : trainer.firstName != null) return false;
        if (lastName != null ? !lastName.equals(trainer.lastName) : trainer.lastName != null) return false;
        if (username != null ? !username.equals(trainer.username) : trainer.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = trainerId != null ? trainerId.hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
