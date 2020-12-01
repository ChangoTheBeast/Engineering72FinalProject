package com.sparta.eng68.traineetracker.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.eng68.traineetracker.entities.Trainee;
import com.sparta.eng68.traineetracker.repositories.TraineeRepository;
import jdk.internal.module.ModuleHashesBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeService {
    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public List<Trainee> getAllTrainees() {
        return (List<Trainee>) traineeRepository.findAll();
    }

    public Optional<Trainee> getTraineeByID(Integer id) {
        return traineeRepository.findById(id);
    }

    public Optional<Trainee> getTraineeByUsername(String username){
        return traineeRepository.findTraineeByUsername(username);
    }

    public Trainee changeTraineeCourseGroupByID(int traineeId, int newGroupId) {
        Trainee trainee = getTraineeByID(traineeId).get();
        trainee.setGroupId(newGroupId);
        trainee = traineeRepository.save(trainee);
        return trainee;
    }

    public List<Trainee> getTraineesByGroupId(int group_id){
        List<Trainee> trainees = traineeRepository.findAllByGroupId(group_id);
        return trainees;
    }

    public void addNewTrainee(Trainee trainee) {
        traineeRepository.save(trainee);
    }

    public void deleteTraineeByID(Integer traineeId) {
        traineeRepository.deleteById(traineeId);
    }
}
