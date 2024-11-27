package lk.ijse.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "log")
public class LogEntity {
    @Id
    private String logId;
    private String logDetails;
    private Date date;
    private String status;
    @Column(columnDefinition = "LONGTEXT")
    private String image2;

    @ManyToMany
    @JoinTable(
            name = "staff_logs_details",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    @JsonManagedReference // Manage the serialization
    private Set<StaffEntity> staffLogs = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "field_logs",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "field_id")
    )
    @JsonManagedReference // Manage the serialization
    private Set<FieldEntity> fieldLogs = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "crop_logs",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "crop_id")
    )
    @JsonManagedReference // Manage the serialization
    private Set<CropEntity> cropLogs = new HashSet<>();
}
