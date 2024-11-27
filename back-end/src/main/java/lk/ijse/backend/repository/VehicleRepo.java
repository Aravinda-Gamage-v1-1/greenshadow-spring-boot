package lk.ijse.backend.repository;

import lk.ijse.backend.entity.StaffEntity;
import lk.ijse.backend.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepo extends JpaRepository<VehicleEntity,String> {
    List<VehicleEntity> findByStaff(StaffEntity staff);
}
