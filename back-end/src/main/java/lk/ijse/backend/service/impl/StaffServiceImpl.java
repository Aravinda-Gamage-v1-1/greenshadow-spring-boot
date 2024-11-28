package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.FieldDTO;
import lk.ijse.backend.dto.impl.StaffDTO;
import lk.ijse.backend.entity.FieldEntity;
import lk.ijse.backend.entity.StaffEntity;
import lk.ijse.backend.repository.FieldRepo;
import lk.ijse.backend.repository.StaffRepo;
import lk.ijse.backend.service.StaffService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private FieldRepo fieldRepo;

    @Autowired
    private Mapping mapping;
    
    @Override
    public StaffDTO save(StaffDTO dto) {
        dto.setStaffId(AppUtil.generateStaffId());
        try {
            StaffEntity staffEntity = mapping.toStaffEntity(dto);

            if (dto.getFieldIds() != null && !dto.getFieldIds().isEmpty()) {
                // Retrieve and associate fields
                Set<FieldEntity> associatedFields = new HashSet<>();
                for (String fieldId : dto.getFieldIds()) {
                    FieldEntity field = fieldRepo.findById(fieldId)
                            .orElseThrow(() -> new IllegalArgumentException("Field not found with ID: " + fieldId));
                    associatedFields.add(field);
                }
                staffEntity.setFields(new ArrayList<>(associatedFields));
            }

            // Save the staff entity
            StaffEntity savedStaff = staffRepo.save(staffEntity);

            return mapping.toStaffDTO(savedStaff);
        } catch (Exception e) {
            throw new RuntimeException("Error saving staff: " + e.getMessage(), e);
        }
    }

    @Override
    public StaffDTO update(String id, StaffDTO dto) {
        StaffEntity existingStaff = staffRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));

        // Update fields
        existingStaff.setFirstName(dto.getFirstName());
        existingStaff.setLastName(dto.getLastName());
        existingStaff.setEmail(dto.getEmail());
        existingStaff.setDob(dto.getDob());
        existingStaff.setAddress(dto.getAddress());
        existingStaff.setContact(dto.getContact());
        existingStaff.setJoinDate(dto.getJoinDate());
        existingStaff.setRole(dto.getRole());

        if (dto.getFieldIds() != null && !dto.getFieldIds().isEmpty()) {
            // Clear the current fields association to avoid duplicate entries
            existingStaff.getFields().clear();

            // Retrieve and associate fields
            for (String fieldId : dto.getFieldIds()) {
                FieldEntity field = fieldRepo.findById(fieldId)
                        .orElseThrow(() -> new IllegalArgumentException("Field not found with ID: " + fieldId));

                // Add the staff to the field's staff list (bidirectional association)
                if (!field.getStaffMembers().contains(existingStaff)) {
                    field.getStaffMembers().add(existingStaff);
                }

                // Add the field to the staff's fields list
                existingStaff.getFields().add(field);
            }
        } else {
            // If no fields are provided, clear the association
            existingStaff.getFields().clear();
        }
        // Save the updated entity
        StaffEntity updatedEntity = staffRepo.save(existingStaff);

        // Convert the updated entity back to DTO
        return mapping.toStaffDTO(updatedEntity);
    }

    @Override
    public void delete(String id) {
        staffRepo.deleteById(id);
    }

    @Override
    public StaffDTO findById(String id) {
        Optional<StaffEntity> byId = staffRepo.findById(id);
        if (byId.isPresent()){
            return mapping.toStaffDTO(byId.get());
        }
        return null;
    }

    @Override
    public List<StaffDTO> findAll() {
        return mapping.asStaffDTOList(staffRepo.findAll());
    }

    @Override
    public Optional<StaffDTO> findByEmail(String email) {
        Optional<StaffEntity> byEmail = staffRepo.findByEmail(email);

        return byEmail.map(mapping::toStaffDTO);

    }

    @Override
    public List<FieldDTO> getFieldsOfStaffId(String staffId) {
        StaffEntity staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + staffId));

        return mapping.asFieldDTOList(new ArrayList<>(staff.getFields()));
    }
}
