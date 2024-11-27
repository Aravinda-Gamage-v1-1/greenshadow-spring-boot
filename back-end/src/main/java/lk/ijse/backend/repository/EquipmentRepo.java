package lk.ijse.backend.repository;

import lk.ijse.backend.entity.EquipmentEntity;
import lk.ijse.backend.entity.FieldEntity;
import lk.ijse.backend.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepo extends JpaRepository<EquipmentEntity,String> {
    List<EquipmentEntity> findByStaff(StaffEntity staff);
    List<EquipmentEntity> findByField(FieldEntity field);
}
