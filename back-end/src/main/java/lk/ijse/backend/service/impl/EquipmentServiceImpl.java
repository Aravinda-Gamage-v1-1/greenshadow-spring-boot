package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.EquipmentDTO;
import lk.ijse.backend.entity.CropEntity;
import lk.ijse.backend.entity.EquipmentEntity;
import lk.ijse.backend.entity.FieldEntity;
import lk.ijse.backend.entity.StaffEntity;
import lk.ijse.backend.exception.CropNotFoundException;
import lk.ijse.backend.repository.EquipmentRepo;
import lk.ijse.backend.repository.FieldRepo;
import lk.ijse.backend.repository.StaffRepo;
import lk.ijse.backend.service.EquipmentService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private EquipmentRepo equipmentRepo;
    @Autowired
    private Mapping mapping;
    
    @Override
    public List<EquipmentDTO> getEquipmentByStaffId(String staffId) {
        StaffEntity staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + staffId));
        List<EquipmentEntity> equipmentList = equipmentRepo.findByStaff(staff);
        return mapping.asEquipmentDTOList(equipmentList);
    }

    @Override
    public List<EquipmentDTO> getEquipmentByFieldId(String fieldId) {
        FieldEntity field = fieldRepo.findById(fieldId)
                .orElseThrow(() -> new IllegalArgumentException("Field not found with ID: " + fieldId));
        List<EquipmentEntity> equipmentList = equipmentRepo.findByField(field);
        return mapping.asEquipmentDTOList(equipmentList);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public EquipmentDTO save(EquipmentDTO dto) {
        dto.setEquipmentId(AppUtil.generateEquipmentId());
        EquipmentEntity equipment = mapping.toEquipmentEntity(dto);
        equipment = equipmentRepo.save(equipment);
        return mapping.toEquipmentDTO(equipment);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public EquipmentDTO update(String id, EquipmentDTO dto) {
        EquipmentEntity equipment = equipmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with ID: " + id));

        equipment.setType(dto.getType());
        equipment.setName(dto.getName());
        equipment.setStatus(dto.getStatus());

        if (dto.getFieldId() != null) {
            FieldEntity field = fieldRepo.findById(dto.getFieldId())
                    .orElseThrow(() -> new IllegalArgumentException("Field not found with ID: " + dto.getFieldId()));
            equipment.setField(field);
        }

        if (dto.getStaffId() != null) {
            StaffEntity staff = staffRepo.findById(dto.getStaffId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + dto.getStaffId()));
            equipment.setStaff(staff);
        }

        return mapping.toEquipmentDTO(equipmentRepo.save(equipment));
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public void delete(String id) {
        equipmentRepo.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public EquipmentDTO findById(String id) {
        EquipmentEntity equipment = equipmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found with ID: " + id));
        return mapping.toEquipmentDTO(equipment);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<EquipmentDTO> findAll() {
        return mapping.asEquipmentDTOList(equipmentRepo.findAll());
    }
}
