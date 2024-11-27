package lk.ijse.backend.repository;

import lk.ijse.backend.entity.CropEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepo extends JpaRepository<CropEntity,String> {
}
