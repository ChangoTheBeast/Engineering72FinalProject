package com.sparta.eng72.traineetracker.services;

import com.sparta.eng72.traineetracker.entities.Trainer;
import com.sparta.eng72.traineetracker.repositories.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public List<Trainer> getAllTrainers() {
        return (List<Trainer>) trainerRepository.findAll();
    }

    public Optional<Trainer> getTrainerByID(Integer id) {
        return (Optional<Trainer>) trainerRepository.findById(id);
    }

    public Optional<Trainer> getTrainerByUsername(String username){
        return trainerRepository.findByUsername(username);
    }
}
