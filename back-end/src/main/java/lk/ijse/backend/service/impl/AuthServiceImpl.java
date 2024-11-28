package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.UserDTO;
import lk.ijse.backend.entity.UserEntity;
import lk.ijse.backend.exception.UserNotFoundException;
import lk.ijse.backend.repository.UserRepo;
import lk.ijse.backend.secure.JWTAuthResponse;
import lk.ijse.backend.secure.SignIn;
import lk.ijse.backend.service.AuthService;
import lk.ijse.backend.service.JWTService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final Mapping mapping;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JWTAuthResponse signIn(SignIn signIn) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.getEmail(),signIn.getPassword()));
        var user = userRepo.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not found"));
        var generatedToken = jwtService.generateToken(user);

        return JWTAuthResponse.builder().token(generatedToken).build();
    }

    //save user in db and issue a token
    @Override
    public JWTAuthResponse signUp(UserDTO userDTO) {
        userDTO.setId(AppUtil.generateUserId());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        //save user
        UserEntity user = mapping.toUserEntity(userDTO);
        System.out.println(user);
        UserEntity savedUser = userRepo.save(user);
        // System.out.println(savedUser);
        //generate token
        var token = jwtService.generateToken(savedUser);
        return JWTAuthResponse.builder().token(token).build();
    }

    @Override
    public JWTAuthResponse refreshToken(String accessToken) {
        //extract username from existing token
        var userName= jwtService.extractUserName(accessToken);
        //check the user availability in the db
        var findUser=  userRepo.findByEmail(userName)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));
        var refreshedToken = jwtService.refreshToken(findUser);
        return JWTAuthResponse.builder().token(refreshedToken).build();
    }
}
