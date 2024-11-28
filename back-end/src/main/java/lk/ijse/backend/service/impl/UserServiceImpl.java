package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.UserDTO;
import lk.ijse.backend.entity.UserEntity;
import lk.ijse.backend.exception.UserNotFoundException;
import lk.ijse.backend.repository.UserRepo;
import lk.ijse.backend.service.UserService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo ;
    @Autowired
    private Mapping mapping;

    @Override
    public UserDTO save(UserDTO dto) {
        dto.setId(AppUtil.generateUserId());

        return mapping.toUserDTO(userRepo.save(mapping.toUserEntity(dto)));
    }

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public UserDTO update(String id, UserDTO dto) {
        return mapping.toUserDTO(userRepo.save(mapping.toUserEntity(dto)));
    }

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public void delete(String id) {
        userRepo.deleteById(id);
    }

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public UserDTO findById(String id) {
        return null;
    }

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<UserDTO> findAll() {
        return mapping.asUserDTOList(userRepo.findAll());
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        Optional<UserEntity> byEmail = userRepo.findByEmail(email);

        return byEmail.map(mapping::toUserDTO);
    }

//    @Override
//    public UserDetailsService userDetailService() {
//        return userName ->
//                userRepo.findByEmail(userName)
//                        .orElseThrow(()->new UserNotFoundException("User Not Found"));
//    }
}
