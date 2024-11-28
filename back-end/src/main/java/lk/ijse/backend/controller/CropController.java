package lk.ijse.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.backend.dto.impl.CropDTO;
import lk.ijse.backend.exception.CropNotFoundException;
import lk.ijse.backend.service.CropService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.RegexUtilForId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/crops")
public class CropController {
    @Autowired
    private CropService cropService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveCrop(@RequestParam("cropData") String cropData,
                                      @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        // Convert cropData JSON string to CropDTO object
        CropDTO cropDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            cropDto = objectMapper.readValue(cropData, CropDTO.class);
            // Validate the field ID format
            if (!RegexUtilForId.isValidFieldId(cropDto.getFieldId())) {
                return new ResponseEntity<>("Invalid field ID format", HttpStatus.BAD_REQUEST);
            }

            // Convert the image file to Base64 string if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    cropDto.setImage1(AppUtil.imageToBase64(imageFile.getBytes()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // Save the crop
            CropDTO savedCrop = cropService.save(cropDto);
            return new ResponseEntity<>(savedCrop, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid crop data", HttpStatus.BAD_REQUEST);
        }


    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateCrop(
            @PathVariable("id") String cropId,
            @RequestParam("cropData") String cropData,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile)
    {

        try {
            if (!RegexUtilForId.isValidCropId(cropId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            // Convert fieldData JSON string to FieldDto object
            ObjectMapper objectMapper = new ObjectMapper();
            CropDTO cropDto = objectMapper.readValue(cropData, CropDTO.class);

            // Convert images to Base64 if provided and set them in the DTO
            if (imageFile != null && !imageFile.isEmpty()) {
                cropDto.setImage1(AppUtil.imageToBase64(imageFile.getBytes()));
            }


            // Call the service to update the field
            cropService.update(cropId, cropDto);

            return ResponseEntity.status(HttpStatus.OK).body("Crop updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating Crop: " + e.getMessage());
        }
    }

    @DeleteMapping("/{cropId}")
    public ResponseEntity<String> deleteCrop(@PathVariable("cropId") String cropId) {
        System.out.println("ok");
        try{
            if (!RegexUtilForId.isValidCropId(cropId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                cropService.delete(cropId);
                return new ResponseEntity<>("Crop deleted successfully.", HttpStatus.NO_CONTENT);
            }
        }catch (CropNotFoundException e){
            return new ResponseEntity<>("Crop not found.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CropDTO> getAllUsers(){
        return cropService.findAll();
    }

    @GetMapping(value = "/{cropId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCropById(@PathVariable("cropId") String cropId) {
        // Validate field ID format using RegexUtilForId
        if (!RegexUtilForId.isValidCropId(cropId)) {
            return new ResponseEntity<>( "Crop ID format is invalid", HttpStatus.BAD_REQUEST);
        }

        // Retrieve the field
        CropDTO cropDto = cropService.findById(cropId);
        if (cropDto == null) {
            return new ResponseEntity<>( "Crop not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cropDto, HttpStatus.OK);
    }
}
