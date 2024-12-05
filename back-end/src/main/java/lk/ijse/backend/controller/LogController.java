package lk.ijse.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.backend.dto.impl.LogDTO;
import lk.ijse.backend.exception.FieldNotFoundException;
import lk.ijse.backend.service.LogService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.RegexUtilForId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/logs")
@CrossOrigin
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LogDTO> saveLog(@RequestParam("logData") String logData,
                                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile)
    {
        logger.info("Received request to save log with data: {}", logData);
        try {
            // Convert JSON string to LogDTO object
            ObjectMapper objectMapper = new ObjectMapper();
            LogDTO dto = objectMapper.readValue(logData, LogDTO.class);
            System.out.println(logData);

            // Convert image to Base64 if provided
            if (imageFile != null) {
                dto.setImage2(AppUtil.imageToBase64(imageFile.getBytes()));
                logger.debug("Image successfully converted to Base64");
            }

            // Save the log and return the response
            LogDTO savedLog = logService.save(dto);
            logger.info("Log saved successfully with ID: {}", savedLog.getLogId());
            return new ResponseEntity<>(savedLog, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error saving log: {}", e.getMessage(), e);
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Update log
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateField(
            @PathVariable("id") String logId,
            @RequestParam("logData") String logData,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile)
    {

        try {
            // Convert fieldData JSON string to FieldDto object
            logger.info("Received request to update log with ID: {}", logId);
            ObjectMapper objectMapper = new ObjectMapper();
            LogDTO logDto = objectMapper.readValue(logData, LogDTO.class);

            // Convert images to Base64 if provided and set them in the DTO
            if (imageFile != null && !imageFile.isEmpty()) {
                logDto.setImage2(AppUtil.imageToBase64(imageFile.getBytes()));
                logger.debug("Updated image successfully converted to Base64");
            }


            // Call the service to update the field
            logService.update(logId, logDto);
            logger.info("Log updated successfully with ID: {}", logId);

            return ResponseEntity.status(HttpStatus.OK).body("Log updated successfully");
        } catch (Exception e) {
            logger.error("Error updating log with ID {}: {}", logId, e.getMessage(), e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Log: " + e.getMessage());
        }
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<String> deleteField(@PathVariable("logId") String logId) {
        try{
            logger.info("Received request to delete log with ID: {}", logId);
            if (!RegexUtilForId.isValidLogId(logId)){
                logger.warn("Invalid log ID format: {}", logId);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                logService.delete(logId);
                logger.info("Log deleted successfully with ID: {}", logId);
                return new ResponseEntity<>("Log deleted successfully.", HttpStatus.NO_CONTENT);
            }
        }catch (FieldNotFoundException e){
            return new ResponseEntity<>("Log not found.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error("Error deleting log with ID {}: {}", logId, e.getMessage(), e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LogDTO> getAllUsers(){
        return logService.findAll();
    }

    @GetMapping(value = "/{logId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLogById(@PathVariable("logId") String logId) {
        logger.info("Received request to retrieve all logs");
        // Validate field ID format using RegexUtilForId
        if (!RegexUtilForId.isValidLogId(logId)) {
            logger.info("Received request to retrieve log by ID: {}",logId );
            return new ResponseEntity<>( "Log ID format is invalid", HttpStatus.BAD_REQUEST);
        }

        // Retrieve the field
        LogDTO logDto = logService.findById(logId);
        if (logDto == null) {
            return new ResponseEntity<>( "Log not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(logDto, HttpStatus.OK);
    }

    @GetMapping("/{logId}/related-entities")
    public ResponseEntity<Map<String, Object>> getRelatedEntities(@PathVariable String logId) {
        logger.info("Received request to retrieve related entities for log ID: {}", logId);
        Map<String, Object> relatedEntities = logService.getRelatefdEntitiesAsDtos(logId);
        return ResponseEntity.ok(relatedEntities);
    }
}
