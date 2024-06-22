package escom.ttbackend.presentation.dto;

import escom.ttbackend.model.enums.Role;
import escom.ttbackend.model.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String clinic;
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private String phone;
    private Sex sex;
    private Role role;
    private String parent_email;
}
