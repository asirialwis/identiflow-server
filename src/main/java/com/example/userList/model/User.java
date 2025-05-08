package com.example.userList.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message="Username is required")
    @Size(min=3,max=50,message="Username must be between 3 and 50")
    @Column(unique=true,nullable=false)
    private String username;

    @NotBlank(message="email is required")
    @Email(message="Email should be valid")
    @Column(unique=true,nullable=false)
    private String email;

    @NotBlank(message="NIC is required")
    @Pattern(regexp = "^[0-9]{12}$",message = "NIC must be exactly 12 digits")
    private String nic;

    @NotBlank(message="mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$",message="Mobile number must be exactly 10 digits")
    private String mobile;

    @NotBlank(message="password is required")
    @Size(min=6,message="password must be at least 6 characters")
    private String password;


    @Column(name = "profile_image",nullable = true)
    private String profileImage;

    @Column(nullable=false)
    private boolean status = true;

    @CreationTimestamp
    @Column(name="created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;






}
