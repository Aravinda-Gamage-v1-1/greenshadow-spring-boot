package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.FieldDTO;
import lk.ijse.backend.dto.impl.StaffDTO;
import lk.ijse.backend.entity.FieldEntity;
import lk.ijse.backend.entity.StaffEntity;
import lk.ijse.backend.exception.FieldNotFoundException;
import lk.ijse.backend.repository.FieldRepo;
import lk.ijse.backend.repository.StaffRepo;
import lk.ijse.backend.service.FieldService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FieldServiceImpl implements FieldService {
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private Mapping mapping;
    
    @Override
    public List<StaffDTO> getStaffIdsByFieldId(String fieldId) {
        FieldEntity field = fieldRepo.findById(fieldId)
                .orElseThrow(() -> new IllegalArgumentException("Field not found with ID: " + fieldId));

        return mapping.asStaffDTOList(new ArrayList<>(field.getStaffMembers()));
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public FieldDTO save(FieldDTO dto) {
        dto.setFieldId(AppUtil.generateFieldId());
        FieldEntity field = mapping.toFieldEntity(dto);
        try {
            return mapping.toFieldDTO(fieldRepo.save(field));
        } catch (Exception e) {
            throw new RuntimeException("Error saving field: " + e.getMessage(), e);
        }
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public FieldDTO update(String id, FieldDTO dto) {
        FieldEntity existingField = fieldRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Field not found with ID: " + id));

        // Update basic field properties
        existingField.setName(dto.getName());
        existingField.setSize(dto.getSize());
        existingField.setLocation(dto.getLocation());
        // Update staff members if provided
        if (dto.getStaffIds() != null && !dto.getStaffIds().isEmpty()) {
            List<StaffEntity> staffEntities = staffRepo.findAllById(dto.getStaffIds());
            if (staffEntities.size() != dto.getStaffIds().size()) {
                throw new IllegalArgumentException("One or more staff IDs are invalid.");
            }
            existingField.setStaffMembers(new HashSet<>(staffEntities));
        } else {
            existingField.getStaffMembers().clear();  // Clear existing staff if no IDs are provided
        }

        // Handle images if provided
        if (dto.getImage1() != null) {
            existingField.setImage1(dto.getImage1());
        }

        if (dto.getImage2() != null) {
            existingField.setImage2(dto.getImage2());
        }

        // Save the updated field entity
        return mapping.toFieldDTO(fieldRepo.save(existingField));
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void delete(String id) {
        FieldEntity field = fieldRepo.findById(id)
                .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: "+id ));

        // Remove associations with staff members
        field.getStaffMembers().forEach(staff -> staff.getFields().remove(field));
        field.getLogs().forEach(log -> log.getFieldLogs().remove(field));
        field.getStaffMembers().clear();
        field.getLogs().clear();
        fieldRepo.deleteById(id);
    }

    @Override
    public FieldDTO findById(String id) {
        Optional<FieldEntity> byId = fieldRepo.findById(id);
        if (byId.isPresent()){
            return mapping.toFieldDTO(byId.get());
        }
        return null;
    }

    @Override
    public List<FieldDTO> findAll() {
        return mapping.asFieldDTOList(fieldRepo.findAll());
    }
}
