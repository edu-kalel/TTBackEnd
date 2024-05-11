package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String clinic;
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private String phone;
    private boolean sex;
    private Role role;
    private String parent_email;
}
