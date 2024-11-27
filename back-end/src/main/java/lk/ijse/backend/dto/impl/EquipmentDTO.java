package lk.ijse.backend.dto.impl;

import lk.ijse.backend.dto.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EquipmentDTO implements EquipmentStatus {
    private String equipmentId;
    private String type;
    private String name;
    private String status;
    private String fieldId;
    private String staffId;
    private String remarks;
}
