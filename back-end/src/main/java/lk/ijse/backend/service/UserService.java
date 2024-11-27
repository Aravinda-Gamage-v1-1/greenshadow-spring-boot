package lk.ijse.backend.service;

import lk.ijse.backend.dto.impl.UserDTO;

import java.util.Optional;

public interface UserService extends BaseService<UserDTO> {
    Optional<UserDTO> findByEmail(String email);
    //UserDetailsService userDetailService();
}
