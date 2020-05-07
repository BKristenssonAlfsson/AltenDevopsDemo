package se.alten.demo.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private String username;
    private String password;
    private List<Role> role;
    private Integer active;
    private String permissions;
}