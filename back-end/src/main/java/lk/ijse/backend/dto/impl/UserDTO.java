package lk.ijse.backend.dto.impl;

import lk.ijse.backend.dto.UserStatus;
import lk.ijse.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements UserStatus {
    private String id;
    private String email;
    private String password;
    private Role role;
    private String staffId;
}
