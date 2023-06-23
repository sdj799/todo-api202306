package com.example.todo.userapi.dto.response;

import com.example.todo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Setter @Getter @ToString
@EqualsAndHashCode(of = "email")
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserSignupResponseDTO {

    private String email;
    private String userName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;
    public UserSignupResponseDTO(User user) {
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.joinDate = user.getJoinDate();
    }
}
