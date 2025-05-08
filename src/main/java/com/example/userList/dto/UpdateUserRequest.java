package com.example.userList.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {

    @Size(min=3,max=50,message="Username must be between 3 and 50")
    private String username;

    @Email(message="Email should be valid")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$",message="Mobile number must be exactly 10 digits")
    private String mobile;

    @Size(min=6,message="password must be at least 6 characters")
    private String password;
    private Boolean status;
    private String profileImage;

}
