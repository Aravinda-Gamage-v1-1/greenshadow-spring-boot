package lk.ijse.backend.repository;

import lk.ijse.backend.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepo extends JpaRepository<LogEntity,String> {
}
