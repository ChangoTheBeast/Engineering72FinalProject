package com.sparta.eng72.traineetracker.repositories;

import com.sparta.eng72.traineetracker.entities.Trainer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Integer> {
    Optional<Trainer> findByUsername(String username);
}
