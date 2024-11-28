package lk.ijse.backend.controller;

import lk.ijse.backend.dto.impl.UserDTO;
import lk.ijse.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getAllUsers(){
        return userService.findAll();
    }
    
    @DeleteMapping(value = "/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email){

        //validate email
        if (!Pattern.matches(String.valueOf(Regex.getEmailPattern()),email)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid email format
        }
        // Fetch user by email
        Optional<UserDTO> userDtoOptional = userService.findByEmail(email);
        if (userDtoOptional.isPresent()) {
            // Get the user's ID
            String userId = userDtoOptional.get().getId();
            userService.delete(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User with email not found
        }

    }
    
    @PutMapping(value = "/{email}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("email") String email, @RequestBody UserDTO userDto) {
        // Validate email format
        if (!Pattern.matches(String.valueOf(Regex.getEmailPattern()), email)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid email format
        }

        // Check if the user exists
        Optional<UserDTO> userDtoOptional = userService.findByEmail(email);
        if (userDtoOptional.isPresent()) {
            UserDTO user = userDtoOptional.get();

            // Update fields in userEntity based on userDto
            user.setEmail(userDto.getEmail());  // Assuming email is allowed to be updated
            user.setPassword(userDto.getPassword()); // Update password
            user.setRole(userDto.getRole()); // Update role if necessary


            // Save the updated entity back to the database
            UserDTO updatedUser = userService.update(user.getId(),user);


            return new ResponseEntity<>(updatedUser, HttpStatus.OK); // Success
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User with email not found
        }
    }
}
