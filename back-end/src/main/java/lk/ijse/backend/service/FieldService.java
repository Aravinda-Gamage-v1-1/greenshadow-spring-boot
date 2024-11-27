package lk.ijse.backend.service;

import lk.ijse.backend.dto.impl.FieldDTO;
import lk.ijse.backend.dto.impl.StaffDTO;

import java.util.List;

public interface FieldService extends BaseService<FieldDTO> {
    List<StaffDTO> getStaffIdsByFieldId(String fieldId);
}
