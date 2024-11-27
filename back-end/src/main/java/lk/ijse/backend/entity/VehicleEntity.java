package lk.ijse.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vehicle")
public class VehicleEntity {
    @Id
    private String vehicleId;
    private String plateNumber;
    private String category;
    private String fuelType;
    private String status;
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;
}
