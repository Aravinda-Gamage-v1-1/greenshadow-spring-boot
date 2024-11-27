package lk.ijse.backend.repository;

import lk.ijse.backend.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepo extends JpaRepository<StaffEntity,String> {
    Optional<StaffEntity> findByEmail(String email);
}
