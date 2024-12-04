package lk.ijse.backend.controller;

import lk.ijse.backend.dto.impl.VehicleDTO;
import lk.ijse.backend.exception.VehicleNotFoundException;
import lk.ijse.backend.service.VehicleService;
import lk.ijse.backend.util.RegexUtilForId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vehicles")
@CrossOrigin
public class VehicleController {
    private static final Logger log = LoggerFactory.getLogger(VehicleController.class);
    @Autowired
    private VehicleService vehicleService;

    // Save Vehicle
    @PostMapping
    public ResponseEntity<VehicleDTO> saveVehicle(@RequestBody VehicleDTO vehicleDto) {
        log.info("Request received to save vehicle: {}", vehicleDto);
        System.out.println(vehicleDto);
        VehicleDTO savedVehicle = vehicleService.save(vehicleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }
    // Update Vehicle
    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable String vehicleId, @RequestBody VehicleDTO vehicleDto) {
        log.info("Request received to update vehicle with ID: {}, Data: {}", vehicleId, vehicleDto);
        VehicleDTO updatedVehicle = vehicleService.update(vehicleId, vehicleDto);
        return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
    }
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable("vehicleId") String vehicleId) {
        try{
            log.info("Request received to delete vehicle with ID: {}", vehicleId);
            if (!RegexUtilForId.isValidVehicleId(vehicleId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                vehicleService.delete(vehicleId);
                return new ResponseEntity<>("Vehicle deleted successfully.", HttpStatus.NO_CONTENT);
            }
        }catch (VehicleNotFoundException e){
            return new ResponseEntity<>("vehicle not found.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Error deleting vehicle with ID: {}", vehicleId, e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable String vehicleId) {
        VehicleDTO vehicle = vehicleService.findById(vehicleId);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> getAllUsers(){
        log.info("Request received to retrieve all vehicles.");
        return vehicleService.findAll();
    }

    // Get Vehicles by Staff ID
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByStaffId(@PathVariable String staffId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByStaffId(staffId);
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }
}
