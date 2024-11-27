package lk.ijse.backend.dto.impl;

import lk.ijse.backend.dto.LogStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogDTO implements LogStatus {
    private String logId;
    private String logDetails;
    private Date date;
    private String image2;
    private String status;
    private Set<String> staffIds;
    private Set<String> fieldIds;
    private Set<String> cropIds;
}
