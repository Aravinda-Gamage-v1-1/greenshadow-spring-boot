package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.CropDTO;
import lk.ijse.backend.entity.CropEntity;
import lk.ijse.backend.entity.FieldEntity;
import lk.ijse.backend.exception.CropNotFoundException;
import lk.ijse.backend.repository.CropRepo;
import lk.ijse.backend.repository.FieldRepo;
import lk.ijse.backend.repository.UserRepo;
import lk.ijse.backend.service.CropService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CropServiceImpl implements CropService {
    @Autowired
    private CropRepo cropRepo;
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private Mapping mapping;

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public CropDTO save(CropDTO dto) {
        dto.setId(AppUtil.generateCropId());
        return mapping.toCropDTO(cropRepo.save(mapping.toCropEntity(dto)));
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public CropDTO update(String id, CropDTO dto) {
        CropEntity existingCrop = cropRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found with ID: " + id));

        // Update basic crop properties
        existingCrop.setCommonName(dto.getCommonName());
        existingCrop.setSpecificName(dto.getSpecificName());
        existingCrop.setCategory(dto.getCategory());
        existingCrop.setSeason(dto.getSeason());

        // Set the field if provided in the DTO
        if (dto.getFieldId() != null) {
            FieldEntity field = fieldRepo.findById(dto.getFieldId())
                    .orElseThrow(() -> new IllegalArgumentException("Field not found with ID: " + dto.getFieldId()));
            existingCrop.setField(field);
        }

        // Handle images if provided
        if (dto.getImage1() != null) {
            existingCrop.setImage1(dto.getImage1());
        }

        // Save the updated crop entity
        return mapping.toCropDTO(cropRepo.save(existingCrop));
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void delete(String id) {
        CropEntity crop = cropRepo.findById(id)
                .orElseThrow(() -> new CropNotFoundException("Crop not found with ID: " + id));

        // Remove associations with staff members
        crop.getLogs().forEach(log -> log.getCropLogs().remove(crop));
        crop.getLogs().clear();
        cropRepo.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public CropDTO findById(String id) {
        Optional<CropEntity> byId = cropRepo.findById(id);
        if (byId.isPresent()){
            return mapping.toCropDTO(byId.get());
        }
        return null;
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<CropDTO> findAll() {
        return mapping.asCropDTOList(cropRepo.findAll());
    }
}
