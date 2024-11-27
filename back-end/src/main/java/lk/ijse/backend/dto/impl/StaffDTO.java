package lk.ijse.backend.dto.impl;

import lk.ijse.backend.dto.StaffStatus;
import lk.ijse.backend.entity.Gender;
import lk.ijse.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffDTO implements StaffStatus {
    private String staffId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String designation;
    private String email;
    private Date dob;
    private String address;
    private String contact;
    private Date joinDate;
    private Role role;
    private List<String> fieldIds;
}
