package lk.ijse.backend.service;

import lk.ijse.backend.dto.impl.UserDTO;

public interface AuthService {
    JWTAuthResponse signIn(SignIn signIn);
    JWTAuthResponse signUp(UserDTO userDTO);
    JWTAuthResponse refreshToken(String accessToken);
}
