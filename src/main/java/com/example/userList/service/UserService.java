package com.example.userList.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.userList.dto.LoginRequest;
import com.example.userList.dto.UpdateUserRequest;
import com.example.userList.dto.UserRequest;
import com.example.userList.model.User;
import com.example.userList.repository.UserRepository;
import com.example.userList.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
        user.setNic(userRequest.getNic());
        user.setMobile(userRequest.getMobile());



        // Hash the password using favre bcrypt
        String hashedPassword = BCrypt.withDefaults().hashToString(12, userRequest.getPassword().toCharArray());
        user.setPassword(hashedPassword);


        if (userRequest.getProfileImage() != null && !userRequest.getProfileImage().isEmpty()) {
            user.setProfileImage(userRequest.getProfileImage());
        }

        return userRepository.save(user);

    }

    public  Map<String, String> login(LoginRequest loginRequest)  {
        Optional<User> optionalUser =  userRepository.findByEmail(loginRequest.getEmail());

        if(optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        User user = optionalUser.get();

        if(!user.isStatus()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is deactivated");
        }

        // Verify the password using favre bcrypt
        BCrypt.Result result = BCrypt.verifyer().verify(
                loginRequest.getPassword().toCharArray(),
                user.getPassword()
        );

        if (!result.verified) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
        }

        String token = JwtUtil.generateToken(user.getId());
        return Map.of("token", token);
    }


    public User getUserProfile(String token) {
        // Extract userId from token
        long userId = Long.parseLong(JwtUtil.extractUserId(token));

        // Find user by ID
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //get user list without log in user
    public List<User> getAllUsersExceptLoggedIn(String token) {
        long loggedInUserId = Long.parseLong(JwtUtil.extractUserId(token));
        return userRepository.findAllByIdNot(loggedInUserId);
    }


    public User updateUser(Long userId, UpdateUserRequest updateRequest) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (updateRequest.getUsername() != null) {
            user.setUsername(updateRequest.getUsername());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getMobile() != null) {
            user.setMobile(updateRequest.getMobile());
        }
        if (updateRequest.getPassword() != null) {
            String hashedPassword = BCrypt.withDefaults().hashToString(12, updateRequest.getPassword().toCharArray());
            user.setPassword(hashedPassword);
        }
        if(updateRequest.getStatus() != null){
            user.setStatus(updateRequest.getStatus());
        }

        if(updateRequest.getProfileImage() != null){
            user.setProfileImage(updateRequest.getProfileImage());
        }

        return userRepository.save(user);
    }

    //delete user from the list
    public void deleteUser(Long userId) {
        if(!userRepository.existsById(userId)){
            throw new RuntimeException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }



}
