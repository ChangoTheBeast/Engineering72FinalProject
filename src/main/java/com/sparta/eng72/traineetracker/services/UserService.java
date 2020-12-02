package com.sparta.eng72.traineetracker.services;

import com.sparta.eng72.traineetracker.entities.User;
import com.sparta.eng72.traineetracker.repositories.UserRepository;
//import com.sparta.eng68.traineetracker.utilities.EmailSender;
import com.sparta.eng72.traineetracker.utilities.PasswordGenerator;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addFirstPassword(String username, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setRole(Role.TRAINEE);

        userRepository.save(user);
    }

    public void changePasswordByUsername(String username, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public Optional<User> getUserOptional(String username){
        return userRepository.findByUsername(username);
    }

    public void addNewUser(String username) {
        User user = new User();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUsername(username);
        String password = PasswordGenerator.generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        PasswordGenerator.writeToFile(username,password);
        user.setRole(Role.FIRST_TIME_USER);

        userRepository.save(user);
    }

    public boolean isPasswordSame(String username, String password){
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        String encodedPassword = user.getPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String encodedPasswordTest = passwordEncoder.encode(password);
        if(passwordEncoder.matches(password,encodedPassword)){
            return true;
        }else{
            return false;
        }
    }

    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    public boolean hasUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public void recoverPassword(String email) {
        Optional<User> userOptional = userRepository.findByUsername(email);
        User user = userOptional.get();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = PasswordGenerator.generatePassword();
//        EmailSender emailSender = new EmailSender();
//        emailSender.email(email,password);
        user.setPassword(passwordEncoder.encode(password));
        PasswordGenerator.writeToFile(email,password);
        user.setRole(Role.FIRST_TIME_USER);
        userRepository.save(user);
    }
}
