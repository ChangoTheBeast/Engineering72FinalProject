package com.sparta.eng68.traineetracker.repositories;


import com.sparta.eng68.traineetracker.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);

}
