package lk.ijse.backend.controller;

import lk.ijse.backend.dto.impl.VehicleDTO;
import lk.ijse.backend.exception.VehicleNotFoundException;
import lk.ijse.backend.service.VehicleService;
import lk.ijse.backend.util.RegexUtilForId;
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
    @Autowired
    private VehicleService vehicleService;

    // Save Vehicle
    @PostMapping
    public ResponseEntity<VehicleDTO> saveVehicle(@RequestBody VehicleDTO vehicleDto) {
        System.out.println(vehicleDto);
        VehicleDTO savedVehicle = vehicleService.save(vehicleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }
    // Update Vehicle
    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable String vehicleId, @RequestBody VehicleDTO vehicleDto) {
        VehicleDTO updatedVehicle = vehicleService.update(vehicleId, vehicleDto);
        return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
    }
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable("vehicleId") String vehicleId) {
        try{
            if (!RegexUtilForId.isValidVehicleId(vehicleId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                vehicleService.delete(vehicleId);
                return new ResponseEntity<>("Vehicle deleted successfully.", HttpStatus.NO_CONTENT);
            }
        }catch (VehicleNotFoundException e){
            return new ResponseEntity<>("vehicle not found.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
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
        return vehicleService.findAll();
    }

    // Get Vehicles by Staff ID
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByStaffId(@PathVariable String staffId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByStaffId(staffId);
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }
}
