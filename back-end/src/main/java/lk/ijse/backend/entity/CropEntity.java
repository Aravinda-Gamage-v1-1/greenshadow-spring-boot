package lk.ijse.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "crop")
public class CropEntity {
    @Id
    private String cropId;

    private String commonName;
    private String specificName;
    private String category;
    private String season;
    @Column(columnDefinition = "LONGTEXT")
    private String image1;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private FieldEntity field;
    @ManyToMany(mappedBy = "cropLogs")
    @JsonBackReference
    private List<LogEntity> logs;
}
