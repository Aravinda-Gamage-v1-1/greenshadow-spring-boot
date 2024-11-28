package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.CropDTO;
import lk.ijse.backend.dto.impl.FieldDTO;
import lk.ijse.backend.dto.impl.LogDTO;
import lk.ijse.backend.dto.impl.StaffDTO;
import lk.ijse.backend.entity.CropEntity;
import lk.ijse.backend.entity.FieldEntity;
import lk.ijse.backend.entity.LogEntity;
import lk.ijse.backend.entity.StaffEntity;
import lk.ijse.backend.repository.CropRepo;
import lk.ijse.backend.repository.FieldRepo;
import lk.ijse.backend.repository.LogRepo;
import lk.ijse.backend.repository.StaffRepo;
import lk.ijse.backend.service.LogService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class LogServiceImpl implements LogService {
    @Autowired
    private LogRepo logRepo;
    @Autowired
    private FieldRepo fieldRepo;
    @Autowired
    private CropRepo cropRepo;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private Mapping mapping;

    private void updateStaffAssociations(LogEntity log, Set<String> staffIds) {
        List<StaffEntity> staffEntities = staffRepo.findAllById(staffIds);
        if (staffEntities.size() != staffIds.size()) {
            throw new IllegalArgumentException("One or more staff IDs are invalid.");
        }

        // Add only new associations
        for (StaffEntity staff : staffEntities) {
            if (!log.getStaffLogs().contains(staff)) {
                log.getStaffLogs().add(staff);
                staff.getLogs().add(log); // Maintain bi-directional association
            }
        }

        // Remove unreferenced associations
        log.getStaffLogs().removeIf(staff -> !staffEntities.contains(staff));
    }

    private void updateFieldAssociations(LogEntity log, Set<String> fieldIds) {
        List<FieldEntity> fieldEntities = fieldRepo.findAllById(fieldIds);
        if (fieldEntities.size() != fieldIds.size()) {
            throw new IllegalArgumentException("One or more field IDs are invalid.");
        }

        // Add only new associations
        for (FieldEntity field : fieldEntities) {
            if (!log.getFieldLogs().contains(field)) {
                log.getFieldLogs().add(field);
                field.getLogs().add(log); // Maintain bi-directional association
            }
        }

        // Remove unreferenced associations
        log.getFieldLogs().removeIf(field -> !fieldEntities.contains(field));
    }

    private void updateCropAssociations(LogEntity log, Set<String> cropIds) {
        List<CropEntity> cropEntities = cropRepo.findAllById(cropIds);
        if (cropEntities.size() != cropIds.size()) {
            throw new IllegalArgumentException("One or more crop IDs are invalid.");
        }

        // Add only new associations
        for (CropEntity crop : cropEntities) {
            if (!log.getCropLogs().contains(crop)) {
                log.getCropLogs().add(crop);
                crop.getLogs().add(log); // Maintain bi-directional association
            }
        }

        // Remove unreferenced associations
        log.getCropLogs().removeIf(crop -> !cropEntities.contains(crop));
    }


    @Override
    public Map<String, Object> getRelatefdEntitiesAsDtos(String logId) {
        Map<String, Object> relatedEntities = new HashMap<>();
        List<FieldDTO> fieldDtos = null;
        List<CropDTO> cropDtos=null;
        List<StaffDTO> staffDtos =null;
        Optional<LogEntity> logEntity = logRepo.findById(logId);
        if (logEntity.isPresent()){
            LogEntity log = logEntity.get();
            // Convert PersistentSet to List
            List<FieldEntity> fieldEntities = new ArrayList<>(log.getFieldLogs());
            List<CropEntity> cropEntities = new ArrayList<>(log.getCropLogs());
            List<StaffEntity> staffEntities = new ArrayList<>(log.getStaffLogs());
            if (!fieldEntities.isEmpty()){
                fieldDtos =  mapping.asFieldDTOList(fieldEntities);
            }
            if ((!cropEntities.isEmpty())){
                cropDtos = mapping.asCropDTOList( cropEntities);
            }
            if (!staffEntities.isEmpty()){
                staffDtos = mapping.asStaffDTOList( staffEntities);
            }

        }
        relatedEntities.put("fields", fieldDtos);
        relatedEntities.put("crops", cropDtos);
        relatedEntities.put("staff", staffDtos);

        return relatedEntities;
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public LogDTO save(LogDTO dto) {
        dto.setLogId(AppUtil.generateLogId());
        LogEntity logEntity = mapping.toLogEntity(dto);
        // Retrieve and set associated staff entities
        if (dto.getStaffIds() != null && !dto.getStaffIds().isEmpty()) {
            Set<StaffEntity> staffEntities = new HashSet<>(staffRepo.findAllById(dto.getStaffIds()));
            if (staffEntities.size() != dto.getStaffIds().size()) {
                throw new IllegalArgumentException("One or more staff IDs are invalid.");
            }
            logEntity.setStaffLogs(staffEntities);
        }
        // Retrieve and set associated field entities
        if (dto.getFieldIds() != null && !dto.getFieldIds().isEmpty()) {
            Set<FieldEntity> fieldEntities = new HashSet<>(fieldRepo.findAllById(dto.getFieldIds()));
            if (fieldEntities.size() != dto.getFieldIds().size()) {
                throw new IllegalArgumentException("One or more field IDs are invalid.");
            }
            logEntity.setFieldLogs(fieldEntities);
        }
        // Retrieve and set associated crop entities
        if (dto.getCropIds() != null && !dto.getCropIds().isEmpty()) {
            Set<CropEntity> cropEntities = new HashSet<>(cropRepo.findAllById(dto.getCropIds()));
            if (cropEntities.size() != dto.getCropIds().size()) {
                throw new IllegalArgumentException("One or more crop IDs are invalid.");
            }
            logEntity.setCropLogs(cropEntities);
        }
        // Save the log entity and map back to DTO
        LogEntity savedLog = logRepo.save(logEntity);
        return mapping.toLogDTO(savedLog);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public LogDTO update(String id, LogDTO dto) {
        LogEntity existingLog = logRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Log not found with ID: " + id));

        // Update basic fields
        existingLog.setLogDetails(dto.getLogDetails());
        existingLog.setDate( dto.getDate());

        // Handle image update if provided
        if (dto.getImage2() != null) {
            existingLog.setImage2(dto.getImage2());
        }
        
        // Update associated staff
        if (dto.getStaffIds() != null && !dto.getStaffIds().isEmpty()) {
            updateStaffAssociations(existingLog, dto.getStaffIds());
        } else {
            existingLog.getStaffLogs().clear(); // Clear if no staff IDs are provided
        }

        // Update associated fields
        if (dto.getFieldIds() != null && !dto.getFieldIds().isEmpty()) {
            updateFieldAssociations(existingLog, dto.getFieldIds());
        } else {
            existingLog.getFieldLogs().clear(); // Clear if no field IDs are provided
        }

        // Update associated crops
        if (dto.getCropIds() != null && !dto.getCropIds().isEmpty()) {
            updateCropAssociations(existingLog, dto.getCropIds());
        } else {
            existingLog.getCropLogs().clear(); // Clear if no crop IDs are provided
        }


        // Save the updated log entity and return the updated DTO
        return mapping.toLogDTO(logRepo.save(existingLog));
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void delete(String id) {
        logRepo.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public LogDTO findById(String id) {
        Optional<LogEntity> byId = logRepo.findById(id);
        if (byId.isPresent()){
            return mapping.toLogDTO(byId.get());
        }
        return null;
    }

    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public List<LogDTO> findAll() {
        return  mapping.asLogDTOList(logRepo.findAll());
    }
}
