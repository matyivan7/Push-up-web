package com.project.pushup.entity;


import com.project.pushup.dto.UserCreationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Table(name = "push_up_user")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String userRole = "USER";

    public User(UserCreationDTO userCreationDTO) {
        this.username = userCreationDTO.getUsername();
        this.password = userCreationDTO.getPassword();
        this.userRole = "USER";
    }
}
