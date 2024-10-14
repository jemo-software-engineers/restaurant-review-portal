package com.jemo.RestaurantReviewPortal.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public boolean updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User userToUpdate = this.findUserById(id);
        if(userToUpdate != null) {
            userToUpdate.setUsername(updateUserRequest.username() == null ? userToUpdate.getUsername() : updateUserRequest.username().toLowerCase());
            userToUpdate.setPassword(updateUserRequest.password() == null ? userToUpdate.getPassword() : passwordEncoder.encode(updateUserRequest.password()));
            userToUpdate.setEmail(updateUserRequest.email() == null ? userToUpdate.getEmail() : updateUserRequest.email().toLowerCase());
            userRepository.save(userToUpdate);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteUserById(Long id) {
        User userToDelete = this.findUserById(id);
        if(userToDelete != null) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
