package lk.ijse.backend.service;

import lk.ijse.backend.dto.impl.EquipmentDTO;

import java.util.List;

public interface EquipmentService extends BaseService<EquipmentDTO> {
    List<EquipmentDTO> getEquipmentByStaffId(String staffId);
    List<EquipmentDTO> getEquipmentByFieldId(String fieldId);
}
