package lk.ijse.backend.service;

import lk.ijse.backend.dto.impl.VehicleDTO;

import java.util.List;

public interface VehicleService extends BaseService<VehicleDTO> {
    List<VehicleDTO> getVehiclesByStaffId(String staffId);
}
