package lk.ijse.backend.controller;

import lk.ijse.backend.dto.impl.EquipmentDTO;
import lk.ijse.backend.exception.EquipmentNotFoundException;
import lk.ijse.backend.service.EquipmentService;
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
@RequestMapping("api/v1/equipments")
@CrossOrigin
public class EquipmentController {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);
    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<EquipmentDTO> saveEquipment(@RequestBody EquipmentDTO equipmentDto) {
        logger.info("Request to save equipment: {}", equipmentDto);
        EquipmentDTO createdEquipment = equipmentService.save(equipmentDto);
        logger.info("Equipment saved successfully: {}", createdEquipment);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    @PutMapping("/{equipmentId}")
    public ResponseEntity<EquipmentDTO> updateEquipment(@PathVariable String equipmentId, @RequestBody EquipmentDTO equipmentDto) {
        logger.info("Request to update equipment with ID: {}, data: {}", equipmentId, equipmentDto);
        EquipmentDTO updatedEquipment = equipmentService.update(equipmentId, equipmentDto);
        return ResponseEntity.ok(updatedEquipment);
    }

    @DeleteMapping("/{equipmentId}")
    public ResponseEntity<String> deleteEquipment(@PathVariable("equipmentId") String equipmentId) {
        logger.info("Request to delete equipment with ID: {}", equipmentId);
        try{
            if (!RegexUtilForId.isValidEquipmentId(equipmentId)){
                logger.warn("Invalid equipment ID format: {}", equipmentId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                equipmentService.delete(equipmentId);
                return new ResponseEntity<>("Equipment deleted successfully.", HttpStatus.NO_CONTENT);
            }
        }catch (EquipmentNotFoundException e){
            logger.error("Equipment not found with ID: {}", equipmentId, e);
            return new ResponseEntity<>("Equipment not found.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/{equipmentId}")
    public ResponseEntity<EquipmentDTO> getEquipmentById(@PathVariable String equipmentId) {
        logger.info("Request to fetch equipment with ID: {}", equipmentId);
        EquipmentDTO equipment = equipmentService.findById(equipmentId);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByStaffId(@PathVariable String staffId) {
        logger.info("Request to fetch equipment assigned to staff ID: {}", staffId);
        List<EquipmentDTO> equipmentList = equipmentService.getEquipmentByStaffId(staffId);
        return ResponseEntity.ok(equipmentList);
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByFieldId(@PathVariable String fieldId) {
        logger.info("Request to fetch equipment assigned to field ID: {}", fieldId);
        List<EquipmentDTO> equipmentList = equipmentService.getEquipmentByFieldId(fieldId);
        return ResponseEntity.ok(equipmentList);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDTO> getAllUsers(){
        logger.info("Request to fetch all equipment");
        return equipmentService.findAll();
    }
}
