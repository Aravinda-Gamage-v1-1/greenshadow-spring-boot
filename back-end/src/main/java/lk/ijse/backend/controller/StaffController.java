package lk.ijse.backend.controller;

import lk.ijse.backend.dto.impl.FieldDTO;
import lk.ijse.backend.dto.impl.StaffDTO;
import lk.ijse.backend.exception.StaffNotFoundException;
import lk.ijse.backend.service.StaffService;
import lk.ijse.backend.util.RegexUtilForId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/staffs")
@CrossOrigin
public class StaffController {
    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);
    @Autowired
    private StaffService staffService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public ResponseEntity<StaffDTO> saveStaff(@RequestBody StaffDTO staffDto) {
        logger.info("Request received to save staff: {}", staffDto);
        StaffDTO savedStaff = staffService.save(staffDto);
        logger.info("Staff saved successfully: {}", savedStaff);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStaff);
    }

    @PutMapping(value = "/{staffId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public ResponseEntity<StaffDTO> updateStaff(@PathVariable("staffId") String staffId, @RequestBody StaffDTO staffDto) {
        logger.info("Request received to update staff with ID: {}, Data: {}", staffId, staffDto);
        StaffDTO updatedStaff = staffService.update(staffId, staffDto);
        logger.info("Staff updated successfully: {}", updatedStaff);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{staffId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public ResponseEntity<String> deleteStaff(@PathVariable("staffId") String staffId) {
        try{
            logger.info("Request received to delete staff with ID: {}", staffId);
            if (!RegexUtilForId.isValidStaffId(staffId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                staffService.delete(staffId);
                return new ResponseEntity<>("Staff deleted successfully.", HttpStatus.NO_CONTENT);
            }
        }catch (StaffNotFoundException e){
            return new ResponseEntity<>("Staff not found.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("Error deleting staff with ID: {}", staffId, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<StaffDTO> getAllUsers(){
        return staffService.findAll();
    }

    @GetMapping(value = "/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public ResponseEntity<?> getFieldById(@PathVariable("staffId") String staffId) {
        // Validate field ID format using RegexUtilForId
        if (!RegexUtilForId.isValidStaffId(staffId)) {
            return new ResponseEntity<>( "Staff ID format is invalid", HttpStatus.BAD_REQUEST);
        }

        // Retrieve the field
        StaffDTO staffDto = staffService.findById(staffId);
        if (staffDto == null) {
            return new ResponseEntity<>( "Staff not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(staffDto, HttpStatus.OK);
    }

    @GetMapping("/{staffId}/field")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public ResponseEntity<List<FieldDTO>> getFieldsOfStaffId(@PathVariable("staffId") String staffId) {
        logger.info("Request received to retrieve fields for staff ID: {}", staffId);
        List<FieldDTO> fieldDtos = staffService.getFieldsOfStaffId(staffId);
        return ResponseEntity.ok(fieldDtos);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public ResponseEntity<StaffDTO> getStaffByEmail(@PathVariable("email") String email) {
        logger.info("Staff retrieved successfully by email: {}", email);
        Optional<StaffDTO> staffDto = staffService.findByEmail(email);
        logger.warn("Staff not found with email: {}", email);
        return ResponseEntity.ok(staffDto.get());
    }
}
