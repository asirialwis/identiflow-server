package com.example.userList.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {

    private String username;
    private String email;
    private String mobile;
    private String password;
    private Boolean status;
    private MultipartFile profileImage;

}
