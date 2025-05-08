package com.example.userList.controller;

import com.example.userList.dto.LoginRequest;
import com.example.userList.dto.UpdateUserRequest;
import com.example.userList.dto.UserRequest;
import com.example.userList.model.User;
import com.example.userList.service.UserService;
import com.example.userList.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) throws IOException {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            // Remove "Bearer " prefix if it exists
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            User user = userService.getUserProfile(token);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Get All users for the List
    @GetMapping
    public ResponseEntity<?> getAllUsersExceptMe(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            List<User> users = userService.getAllUsersExceptLoggedIn(token);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization")String authHeader, @PathVariable Long id, @RequestBody @Valid UpdateUserRequest updateRequest) {

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        if(!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        try {
            User updatedUser = userService.updateUser(id, updateRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestHeader("Authorization")String authHeader ,@PathVariable Long id) {

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or tampered token"));
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }






}
