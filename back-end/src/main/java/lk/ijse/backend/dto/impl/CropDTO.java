package lk.ijse.backend.dto.impl;

import lk.ijse.backend.dto.CropStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CropDTO implements CropStatus {
    private String id;
    private String commonName;
    private String specificName;
    private String category;
    private String season;
    private String image1;
    private String fieldId;
}
