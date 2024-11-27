package lk.ijse.backend.dto.impl;

import lk.ijse.backend.dto.FieldStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldDTO implements FieldStatus {
    private String fieldId;
    private String name;
    private String location;
    private Double size;
    private String image1;
    private String image2;
    private Set<String> staffIds;
}
