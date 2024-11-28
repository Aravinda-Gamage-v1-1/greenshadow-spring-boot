package lk.ijse.backend.controller;

import lk.ijse.backend.dto.impl.EquipmentDTO;
import lk.ijse.backend.exception.EquipmentNotFoundException;
import lk.ijse.backend.service.EquipmentService;
import lk.ijse.backend.util.RegexUtilForId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/equipments")
@CrossOrigin
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;
    @PostMapping
    public ResponseEntity<EquipmentDTO> saveEquipment(@RequestBody EquipmentDTO equipmentDto) {
        EquipmentDTO createdEquipment = equipmentService.save(equipmentDto);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }
    @PutMapping("/{equipmentId}")
    public ResponseEntity<EquipmentDTO> updateEquipment(@PathVariable String equipmentId, @RequestBody EquipmentDTO equipmentDto) {
        EquipmentDTO updatedEquipment = equipmentService.update(equipmentId, equipmentDto);
        return ResponseEntity.ok(updatedEquipment);
    }
    @DeleteMapping("/{equipmentId}")
    public ResponseEntity<String> deleteEquipment(@PathVariable("equipmentId") String equipmentId) {
        try{
            if (!RegexUtilForId.isValidEquipmentId(equipmentId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                equipmentService.delete(equipmentId);
                return new ResponseEntity<>("Equipment deleted successfully.", HttpStatus.NO_CONTENT);
            }
        }catch (EquipmentNotFoundException e){
            return new ResponseEntity<>("Equipment not found.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @GetMapping("/{equipmentId}")
    public ResponseEntity<EquipmentDTO> getEquipmentById(@PathVariable String equipmentId) {
        EquipmentDTO equipment = equipmentService.findById(equipmentId);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByStaffId(@PathVariable String staffId) {
        List<EquipmentDTO> equipmentList = equipmentService.getEquipmentByStaffId(staffId);
        return ResponseEntity.ok(equipmentList);
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByFieldId(@PathVariable String fieldId) {
        List<EquipmentDTO> equipmentList = equipmentService.getEquipmentByFieldId(fieldId);
        return ResponseEntity.ok(equipmentList);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDTO> getAllUsers(){
        return equipmentService.findAll();
    }
}
