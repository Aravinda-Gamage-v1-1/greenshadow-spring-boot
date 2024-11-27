package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.CropDTO;
import lk.ijse.backend.entity.CropEntity;
import lk.ijse.backend.entity.FieldEntity;
import lk.ijse.backend.repository.CropRepo;
import lk.ijse.backend.repository.FieldRepo;
import lk.ijse.backend.repository.UserRepo;
import lk.ijse.backend.service.CropService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CropDTO save(CropDTO dto) {
        dto.setId(AppUtil.generateCropId());
        return mapping.toCropDTO(cropRepo.save(mapping.toCropEntity(dto)));
    }

    @Override
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
    public void delete(String id) {
        cropRepo.deleteById(id);
    }

    @Override
    public CropDTO findById(String id) {
        Optional<CropEntity> byId = cropRepo.findById(id);
        if (byId.isPresent()){
            return mapping.toCropDTO(byId.get());
        }
        return null;
    }

    @Override
    public List<CropDTO> findAll() {
        return mapping.asCropDTOList(cropRepo.findAll());
    }
}
