package com.example.userList.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "NIC is required")
    @Pattern(regexp = "^[0-9]{12}$",message = "NIC must be exactly 12 digits")
    private String nic;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    private String mobile;

    @NotBlank
    @Size(min=6)
    private String password;

    private String profileImage;
    private boolean status = true;


}
