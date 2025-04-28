package com.example.userList.service;

import com.example.userList.dto.LoginRequest;
import com.example.userList.dto.UserRequest;
import com.example.userList.model.User;
import com.example.userList.repository.UserRepository;
import com.example.userList.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserRequest userRequest) throws IOException {

        if(userRepository.existsByUsername(userRequest.getUsername())) {
            throw new RuntimeException("username already exists");
        }
        if(userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("email already exists");
        }
        User user = new User();

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setMobile(userRequest.getMobile());
        user.setPassword(userRequest.getPassword());


        if (userRequest.getProfileImage() != null && !userRequest.getProfileImage().isEmpty()) {
            user.setProfileImage(userRequest.getProfileImage().getBytes());
        }

        return userRepository.save(user);

    }

    public String login(LoginRequest loginRequest)  {
        Optional<User> optionalUser =  userRepository.findByEmail(loginRequest.getEmail());

        if(optionalUser.isEmpty()){
            throw new RuntimeException("user not found");
        }
        User user = optionalUser.get();

        if(!user.getPassword().equals(loginRequest.getPassword())){
            throw new RuntimeException("wrong password");
        }
        if(!user.isStatus()){
            throw new RuntimeException("User is deactivated");
        }

        return JwtUtil.generateToken(user.getId());
    }


    public User getUserProfile(String token) {
        // Extract userId from token
        long userId = Long.parseLong(JwtUtil.extractUserId(token));

        // Find user by ID
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
