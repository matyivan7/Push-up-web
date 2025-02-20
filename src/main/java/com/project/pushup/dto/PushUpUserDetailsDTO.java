package com.project.pushup.dto;

import com.project.pushup.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushUpUserDetailsDTO {

    private String username;

    private String password;

    private String userRole;

    public PushUpUserDetailsDTO(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userRole =  "USER";
    }
}
