package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.impl.VehicleDTO;
import lk.ijse.backend.entity.StaffEntity;
import lk.ijse.backend.entity.VehicleEntity;
import lk.ijse.backend.repository.StaffRepo;
import lk.ijse.backend.repository.VehicleRepo;
import lk.ijse.backend.service.VehicleService;
import lk.ijse.backend.util.AppUtil;
import lk.ijse.backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private StaffRepo staffRepo;
    @Autowired
    private Mapping vehicleMapper;

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public VehicleDTO save(VehicleDTO dto) {
        System.out.println(dto);
        dto.setVehicleId(AppUtil.generateVehicleId());
        VehicleEntity vehicle = vehicleMapper.toVehicleEntity(dto);
        if (dto.getStaffId() != null) {
            StaffEntity staff = staffRepo.findById(dto.getStaffId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + dto.getStaffId()));
            vehicle.setStaff(staff);
        }
        return vehicleMapper.toVehicleDTO(vehicleRepo.save(vehicle));
    }

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public VehicleDTO update(String id, VehicleDTO dto) {
        VehicleEntity existingVehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + id));

        // Update properties
        existingVehicle.setPlateNumber(dto.getPlateNumber());
        existingVehicle.setCategory(dto.getCategory());
        existingVehicle.setFuelType(dto.getFuelType());
        existingVehicle.setStatus(dto.getStatus());
        existingVehicle.setRemarks(dto.getRemarks());

        // Update associated staff
        if (dto.getStaffId() != null) {
            StaffEntity staff = staffRepo.findById(dto.getStaffId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + dto.getStaffId()));
            existingVehicle.setStaff(staff);
        } else {
            existingVehicle.setStaff(null); // Clear staff if not provided
        }

        return vehicleMapper.toVehicleDTO(vehicleRepo.save(existingVehicle));
    }

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public void delete(String id) {
        vehicleRepo.deleteById(id);
    }

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public VehicleDTO findById(String id) {
        VehicleEntity vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + id));
        return vehicleMapper.toVehicleDTO(vehicle);
    }

    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<VehicleDTO> findAll() {
        return vehicleMapper.asVehicleDTOList(vehicleRepo.findAll());
    }
    // Get Vehicles by Staff ID
    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<VehicleDTO> getVehiclesByStaffId(String staffId) {
        StaffEntity staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + staffId));
        List<VehicleEntity> vehicles = vehicleRepo.findByStaff(staff);
        return vehicleMapper.asVehicleDTOList(vehicles);
    }
}
