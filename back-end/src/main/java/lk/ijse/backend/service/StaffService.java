package lk.ijse.backend.service;

import lk.ijse.backend.dto.impl.FieldDTO;
import lk.ijse.backend.dto.impl.StaffDTO;

import java.util.List;
import java.util.Optional;

public interface StaffService extends BaseService<StaffDTO> {
    Optional<StaffDTO>findByEmail(String email);
    List<FieldDTO> getFieldsOfStaffId(String staffId);
}
