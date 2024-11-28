package lk.ijse.backend.service;

import lk.ijse.backend.dto.impl.UserDTO;
import lk.ijse.backend.secure.JWTAuthResponse;
import lk.ijse.backend.secure.SignIn;

public interface AuthService {
    JWTAuthResponse signIn(SignIn signIn);
    JWTAuthResponse signUp(UserDTO userDTO);
    JWTAuthResponse refreshToken(String accessToken);
}
